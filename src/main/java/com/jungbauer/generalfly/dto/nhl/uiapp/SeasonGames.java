package com.jungbauer.generalfly.dto.nhl.uiapp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SeasonGames {
    private SeasonView season;
    private List<GameView> games;
}
