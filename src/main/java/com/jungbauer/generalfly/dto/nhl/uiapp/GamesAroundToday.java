package com.jungbauer.generalfly.dto.nhl.uiapp;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GamesAroundToday {
    private List<GameView> yesterday = new ArrayList<>();
    private List<GameView> today = new ArrayList<>();
    private List<GameView> tomorrow = new ArrayList<>();

    public void addYesterday(GameView game) {
        this.yesterday.add(game);
    }

    public void addToday(GameView game) {
        this.today.add(game);
    }

    public void addTomorrow(GameView game) {
        this.tomorrow.add(game);
    }
}

