package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GameCenterPlayByPlay {
    private long id;
    private long season;
    private int gameType;
    private boolean limitedScoring;
    private String gameDate;
    private Venue venue;
    private Venue venueLocation;
    private String startTimeUTC;
    private String easternUTCOffset;
    private String venueUTCOffset;
    private List<TvBroadcast> tvBroadcasts;
    private String gameState;
    private String gameScheduleState;
    private PeriodDescriptor periodDescriptor;
    private Team awayTeam;
    private Team homeTeam;
    private boolean shootoutInUse;
    private boolean otInUse;
    private Clock clock;
    private int displayPeriod;
    private int maxPeriods;
    private GameOutcome gameOutcome;
    private List<Play> plays;
    private List<RosterSpot> rosterSpots;
    private int regPeriods;
    private Map<String, Object> summary;

    @Data
    public static class Venue {
        private String _default;
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
    public static class PeriodDescriptor {
        private int number;
        private String periodType;
        private int maxRegulationPeriods;
    }

    @Data
    public static class Team {
        private int id;
        private Map<String, String> commonName;
        private String abbrev;
        private Integer score;
        private Integer sog;
        private String logo;
        private String darkLogo;
        private Map<String, String> placeName;
        private Map<String, String> placeNameWithPreposition;
    }

    @Data
    public static class Clock {
        private String timeRemaining;
        private int secondsRemaining;
        private boolean running;
        private boolean inIntermission;
    }

    @Data
    public static class GameOutcome {
        private String lastPeriodType;
    }

    @Data
    public static class Play {
        private int eventId;
        private PeriodDescriptor periodDescriptor;
        private String timeInPeriod;
        private String timeRemaining;
        private String situationCode;
        private String homeTeamDefendingSide;
        private int typeCode;
        private String typeDescKey;
        private int sortOrder;
        private Details details;
        private String pptReplayUrl;
    }

    @Data
    public static class Details {
        private Integer eventOwnerTeamId;
        private Integer losingPlayerId;
        private Integer winningPlayerId;
        private Integer xCoord;
        private Integer yCoord;
        private String zoneCode;
        private String shotType;
        private Integer shootingPlayerId;
        private Integer goalieInNetId;
        private Integer awaySOG;
        private Integer homeSOG;
        private Integer hittingPlayerId;
        private Integer hitteePlayerId;
        private String reason;
        private String secondaryReason;
        private Integer playerId;
        private String typeCode;
        private String descKey;
        private Integer duration;
        private Integer committedByPlayerId;
        private Integer drawnByPlayerId;
        private Integer blockingPlayerId;
        private Integer servedByPlayerId;
        private Integer scoringPlayerId;
        private Integer scoringPlayerTotal;
        private Integer assist1PlayerId;
        private Integer assist1PlayerTotal;
        private Integer assist2PlayerId;
        private Integer assist2PlayerTotal;
        private Integer homeScore;
        private Integer awayScore;
        private String highlightClipSharingUrl;
        private String highlightClipSharingUrlFr;
        private Long highlightClip;
        private Long highlightClipFr;
        private Long discreteClip;
        private Long discreteClipFr;
        private String homeTeamDefendingSide;
        private String pptReplayUrl;
    }

    @Data
    public static class RosterSpot {
        private int teamId;
        private long playerId;
        private Map<String, String> firstName;
        private Map<String, String> lastName;
        private int sweaterNumber;
        private String positionCode;
        private String headshot;
    }
}