package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ClubSeasonSchedule {
    private long previousSeason;
    private long currentSeason;
    private String clubTimezone;
    private String clubUTCOffset;
    private List<Game> games;

    @Data
    public static class Game {
        private long id;
        private long season;
        private int gameType;
        private String gameDate;
        private Venue venue;
        private boolean neutralSite;
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
        private Player winningGoalie;
        private Player winningGoalScorer;
        private String threeMinRecap;
        private String threeMinRecapFr;
        private String condensedGame;
        private String condensedGameFr;
        private String gameCenterLink;
        private String ticketsLink;
        private String ticketsLinkFr;
    }

    @Data
    public static class Venue {
        private String _default;
        private String fr;
        private String es;

        public String getDefault() { return _default; }
        public void setDefault(String _default) { this._default = _default; }
    }

    @Data
    public static class TvBroadcast {
        private int id;
        private String market;
        private String countryCode;
        private String network;
        private int sequenceNumber;
    }

    @Data
    public static class Team {
        private int id;
        private Map<String, String> commonName;
        private Map<String, String> placeName;
        private Map<String, String> placeNameWithPreposition;
        private String abbrev;
        private String logo;
        private String darkLogo;
        private boolean awaySplitSquad;
        private boolean homeSplitSquad;
        private Integer score;
        private String radioLink;
        private String promoLink;
        private String promoDesc;
        private String airlineLink;
        private String airlineDesc;
        private String hotelLink;
        private String hotelDesc;
    }

    @Data
    public static class PeriodDescriptor {
        private String periodType;
        private int maxRegulationPeriods;
    }

    @Data
    public static class GameOutcome {
        private String lastPeriodType;
    }

    @Data
    public static class Player {
        private long playerId;
        private Map<String, String> firstInitial;
        private Map<String, String> lastName;
    }
}
