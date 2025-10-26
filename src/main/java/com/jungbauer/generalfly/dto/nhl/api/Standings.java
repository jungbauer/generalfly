package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Standings {
    private boolean wildCardIndicator;
    private String standingsDateTimeUtc;
    private List<StandingsTeam> standings;
}
