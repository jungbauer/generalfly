package com.jungbauer.generalfly.controller.nhl;

import com.jungbauer.generalfly.dto.nhl.api.ClubSeasonSchedule;
import com.jungbauer.generalfly.dto.nhl.api.GameCenterPlayByPlay;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
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

    public NhlController(NhlApiService nhlApiService) {
        this.nhlApiService = nhlApiService;
    }

    @GetMapping("/standings")
    public ResponseEntity<Standings> standings() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getStandingsNow());
    }

    @GetMapping("/club-schedule")
    public ResponseEntity<ClubSeasonSchedule> clubSchedule(@RequestParam(name = "team") String teamCode, @RequestParam(name = "season") String season) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getClubSeasonSchedule(teamCode, season));
    }

    @GetMapping("/gamecenter-playbyplay")
    public ResponseEntity<GameCenterPlayByPlay> gamecenterPlayByPlay(@RequestParam(name = "gameId") String gameId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(12, TimeUnit.HOURS))
                .body(nhlApiService.getGameCenterPlayByPlay(gameId));
    }
}
