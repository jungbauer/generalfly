package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Seasons {
    private Integer total;
    private List<SeasonDetails> data;

    @Data
    public static class SeasonDetails {
        private int id;
        private int allStarGameInUse;
        private int conferencesInUse;
        private int divisionsInUse;
        private LocalDateTime endDate;
        private int entryDraftInUse;
        private String formattedSeasonId;
        private int minimumPlayoffMinutesForGoalieStatsLeaders;
        private int minimumRegularGamesForGoalieStatsLeaders;
        private int nhlStanleyCupOwner;
        private int numberOfGames;
        private int olympicsParticipation;
        private int pointForOTLossInUse;
        private LocalDateTime preseasonStartdate;
        private LocalDateTime regularSeasonEndDate;
        private int rowInUse;
        private int seasonOrdinal;
        private LocalDateTime startDate;
        private int supplementalDraftInUse;
        private int tiesInUse;
        private int totalPlayoffGames;
        private int totalRegularSeasonGames;
        private int wildcardInUse;
    }
}
