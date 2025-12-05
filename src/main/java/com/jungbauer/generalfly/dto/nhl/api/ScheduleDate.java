package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ScheduleDate {
    private String nextStartDate;
    private String previousStartDate;
    private List<GameWeek> gameWeek;
    private List<OddsPartner> oddsPartners;
    private String preSeasonStartDate;
    private String regularSeasonStartDate;
    private String regularSeasonEndDate;
    private String playoffEndDate;
    private Integer numberOfGames;

    @Getter
    @Setter
    public static class GameWeek {
        private String date;
        private String dayAbbrev;
        private Integer numberOfGames;
        private List<Object> datePromo;
        private List<Game> games;
    }

    @Getter
    @Setter
    public static class Game {
        private Long id;
        private Long season;
        private Integer gameType;
        private Map<String, String> venue;
        private Boolean neutralSite;
        private String startTimeUTC;
        private String easternUTCOffset;
        private String venueUTCOffset;
        private String venueTimezone;
        private String gameState;
        private String gameScheduleState;
        private List<TvBroadcast> tvBroadcasts;
        private Team awayTeam;
        private Team homeTeam;
        private PeriodDescriptor periodDescriptor;
        private GameOutcome gameOutcome;
        private WinningPlayer winningGoalie;
        private WinningPlayer winningGoalScorer;
        private String threeMinRecap;
        private String threeMinRecapFr;
        private String condensedGame;
        private String condensedGameFr;
        private String ticketsLink;
        private String ticketsLinkFr;
        private String gameCenterLink;
        private Integer displayPeriod;
        private Integer maxPeriods;
        private Integer regPeriods;
        private Boolean shootoutInUse;
        private Boolean otInUse;
        private Boolean tiesInUse;
    }

    @Getter
    @Setter
    public static class TvBroadcast {
        private Integer id;
        private String market;
        private String countryCode;
        private String network;
        private Integer sequenceNumber;
    }

    @Getter
    @Setter
    public static class Team {
        private Integer id;
        private Map<String, String> commonName;
        private Map<String, String> placeName;
        private Map<String, String> placeNameWithPreposition;
        private String abbrev;
        private String logo;
        private String darkLogo;
        private Boolean awaySplitSquad;
        private Boolean homeSplitSquad;
        private Integer score;
        private String radioLink;
    }

    @Getter
    @Setter
    public static class PeriodDescriptor {
        private Integer number;
        private String periodType;
        private Integer maxRegulationPeriods;
    }

    @Getter
    @Setter
    public static class GameOutcome {
        private String lastPeriodType;
    }

    @Getter
    @Setter
    public static class WinningPlayer {
        private Long playerId;
        private Map<String, String> firstInitial;
        private Map<String, String> lastName;
    }

    @Getter
    @Setter
    public static class OddsPartner {
        private Integer partnerId;
        private String country;
        private String name;
        private String imageUrl;
        private String siteUrl;
        private String bgColor;
        private String textColor;
        private String accentColor;
    }
}
