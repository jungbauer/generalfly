package com.jungbauer.generalfly.controller.nhl;

import com.jungbauer.generalfly.dto.nhl.api.ClubSeasonSchedule;
import com.jungbauer.generalfly.dto.nhl.api.GameCenterPlayByPlay;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.dto.nhl.uiapp.GamesAroundToday;
import com.jungbauer.generalfly.service.DumpLogService;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
import com.jungbauer.generalfly.service.nhl.NhlDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/nhl")
@Validated
public class NhlController {
    private final NhlApiService nhlApiService;
    private final NhlDataService nhlDataService;
    private final DumpLogService dumpLogService;

    @Value("${nhl.cache.hours:12}")
    private int cacheHours;

    public NhlController(NhlApiService nhlApiService, NhlDataService nhlDataService, DumpLogService dumpLogService) {
        this.nhlApiService = nhlApiService;
        this.nhlDataService = nhlDataService;
        this.dumpLogService = dumpLogService;
    }

    @GetMapping("/standings")
    public ResponseEntity<Standings> standings(HttpServletRequest request) {
        logEndpointAccess("standings", null, request);
        return withCacheHeaders().body(nhlApiService.getStandingsNow());
    }

    @GetMapping("/club-schedule")
    public ResponseEntity<ClubSeasonSchedule> clubSchedule(
            HttpServletRequest request,
            @RequestParam(name = "team") @Pattern(regexp = "[A-Z]{3}", message = "Team code must be 3 uppercase letters") String teamCode,
            @RequestParam(name = "season") @Pattern(regexp = "\\d{8}", message = "Season must be 8 digits (e.g., 20252026)") String season) {
        logEndpointAccess("clubSchedule", teamCode + " : " + season, request);
        return withCacheHeaders().body(nhlApiService.getClubSeasonSchedule(teamCode, season));
    }

    @GetMapping("/gamecenter-playbyplay")
    public ResponseEntity<GameCenterPlayByPlay> gamecenterPlayByPlay(
            HttpServletRequest request,
            @RequestParam(name = "gameId") @Pattern(regexp = "\\d+", message = "Game ID must be numeric") String gameId) {
        logEndpointAccess("gamecenterPlayByPlay", "game: " + gameId, request);
        return withCacheHeaders().body(nhlApiService.getGameCenterPlayByPlay(gameId));
    }

    @GetMapping("/games/around-today")
    public ResponseEntity<GamesAroundToday> getGamesAroundToday(HttpServletRequest request) {
        logEndpointAccess("getGamesAroundToday", null, request);
        return withCacheHeaders().body(nhlDataService.getGamesAroundToday());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        dumpLogService.logMessage("NhlController", "error", "Error: " + ex.getMessage());

        if (ex instanceof IllegalArgumentException || ex instanceof jakarta.validation.ConstraintViolationException) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to fetch NHL data"));
    }

    private void logEndpointAccess(String endpoint, String details, HttpServletRequest request) {
        String message = String.format("%s called%s, ip: %s",
                endpoint,
                details != null ? ": " + details : "",
                getIpAddress(request));
        dumpLogService.logMessage("NhlController", endpoint, message);
    }

    private ResponseEntity.BodyBuilder withCacheHeaders() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(cacheHours, TimeUnit.HOURS));
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
