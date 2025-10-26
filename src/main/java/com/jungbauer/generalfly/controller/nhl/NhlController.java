package com.jungbauer.generalfly.controller.nhl;

import com.jungbauer.generalfly.dto.nhl.api.ClubSeasonSchedule;
import com.jungbauer.generalfly.dto.nhl.api.GameCenterPlayByPlay;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nhl")
public class NhlController {
    private final NhlApiService nhlApiService;

    public NhlController(NhlApiService nhlApiService) {
        this.nhlApiService = nhlApiService;
    }

    @GetMapping("/standings")
    public Standings standings() {
        return nhlApiService.getStandingsNow();
    }

    @GetMapping("/club-schedule")
    public ClubSeasonSchedule clubSchedule(@RequestParam(name = "team") String teamCode, @RequestParam(name = "season") String season) {
        return nhlApiService.getClubSeasonSchedule(teamCode, season);
    }

    @GetMapping("/gamecenter-playbyplay")
    public GameCenterPlayByPlay gamecenterPlayByPlay(@RequestParam(name = "gameId") String gameId) {
        return nhlApiService.getGameCenterPlayByPlay(gameId);
    }
}
