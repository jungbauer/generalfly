package com.jungbauer.generalfly.dto.nhl.uiapp;

import com.jungbauer.generalfly.domain.nhl.Season;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SeasonView {

    private Integer nhlId;
    private String formattedSeasonId;
    private Integer seasonOrdinal;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate preseasonStartDate;
    private LocalDate regularSeasonEndDate;
    private Boolean collected;

    public SeasonView(Season season) {
        this.nhlId = season.getNhlId();
        this.formattedSeasonId = season.getFormattedSeasonId();
        this.seasonOrdinal = season.getSeasonOrdinal();
        this.startDate = season.getStartDate();
        this.endDate = season.getEndDate();
        this.preseasonStartDate = season.getPreseasonStartDate();
        this.regularSeasonEndDate = season.getRegularSeasonEndDate();
        this.collected = season.getCollected();
    }
}
