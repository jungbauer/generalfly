package com.jungbauer.generalfly.controller.nhl;

import com.jungbauer.generalfly.dto.nhl.api.ClubSeasonSchedule;
import com.jungbauer.generalfly.dto.nhl.api.GameCenterPlayByPlay;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.service.DumpLogService;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
import com.jungbauer.generalfly.service.nhl.NhlDataService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/nhl")
public class NhlController {
    private final NhlApiService nhlApiService;
    private final DumpLogService dumpLogService;

    public NhlController(NhlApiService nhlApiService, DumpLogService dumpLogService) {
        this.nhlApiService = nhlApiService;
        this.dumpLogService = dumpLogService;
    }

    @GetMapping("/standings")
    public ResponseEntity<Standings> standings(HttpServletRequest request) {
        dumpLogService.logMessage("NhlController", "standings", "Standings called. ip: " + getIpAddress(request));
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getStandingsNow());
    }

    @GetMapping("/club-schedule")
    public ResponseEntity<ClubSeasonSchedule> clubSchedule(HttpServletRequest request, @RequestParam(name = "team") String teamCode, @RequestParam(name = "season") String season) {
        String msg = "club schedule called: " + teamCode + " : " + season + ", ip: " + getIpAddress(request);
        dumpLogService.logMessage("NhlController", "clubSchedule", msg);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getClubSeasonSchedule(teamCode, season));
    }

    @GetMapping("/gamecenter-playbyplay")
    public ResponseEntity<GameCenterPlayByPlay> gamecenterPlayByPlay(HttpServletRequest request, @RequestParam(name = "gameId") String gameId) {
        String msg = "pbp for game: " + gameId + ", ip: " + getIpAddress(request);
        dumpLogService.logMessage("NhlController", "gamecenterPlayByPlay", msg);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getGameCenterPlayByPlay(gameId));
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
