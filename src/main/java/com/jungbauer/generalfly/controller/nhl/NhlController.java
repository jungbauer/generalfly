package com.jungbauer.generalfly.controller.nhl;

import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
