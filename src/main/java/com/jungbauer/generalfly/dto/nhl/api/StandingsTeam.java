package com.jungbauer.generalfly.dto.nhl.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandingsTeam {
    private String clinchIndicator;
    private String conferenceAbbrev;
    private int conferenceHomeSequence;
    private int conferenceL10Sequence;
    private String conferenceName;
    private int conferenceRoadSequence;
    private int conferenceSequence;
    private String date;
    private String divisionAbbrev;
    private int divisionHomeSequence;
    private int divisionL10Sequence;
    private String divisionName;
    private int divisionRoadSequence;
    private int divisionSequence;
    private int gameTypeId;
    private int gamesPlayed;
    private int goalDifferential;
    private double goalDifferentialPctg;
    private int goalAgainst;
    private int goalFor;
    private double goalsForPctg;
    private int homeGamesPlayed;
    private int homeGoalDifferential;
    private int homeGoalsAgainst;
    private int homeGoalsFor;
    private int homeLosses;
    private int homeOtLosses;
    private int homePoints;
    private int homeRegulationPlusOtWins;
    private int homeRegulationWins;
    private int homeTies;
    private int homeWins;
    private int l10GamesPlayed;
    private int l10GoalDifferential;
    private int l10GoalsAgainst;
    private int l10GoalsFor;
    private int l10Losses;
    private int l10OtLosses;
    private int l10Points;
    private int l10RegulationPlusOtWins;
    private int l10RegulationWins;
    private int l10Ties;
    private int l10Wins;
    private int leagueHomeSequence;
    private int leagueL10Sequence;
    private int leagueRoadSequence;
    private int leagueSequence;
    private int losses;
    private int otLosses;
    private PlaceName placeName;
    private double pointPctg;
    private int points;
    private double regulationPlusOtWinPctg;
    private int regulationPlusOtWins;
    private double regulationWinPctg;
    private int regulationWins;
    private int roadGamesPlayed;
    private int roadGoalDifferential;
    private int roadGoalsAgainst;
    private int roadGoalsFor;
    private int roadLosses;
    private int roadOtLosses;
    private int roadPoints;
    private int roadRegulationPlusOtWins;
    private int roadRegulationWins;
    private int roadTies;
    private int roadWins;
    private long seasonId;
    private int shootoutLosses;
    private int shootoutWins;
    private String streakCode;
    private int streakCount;
    private TeamName teamName;
    private TeamCommonName teamCommonName;
    private TeamAbbrev teamAbbrev;
    private String teamLogo;
    private int ties;
    private int waiversSequence;
    private int wildcardSequence;
    private double winPctg;
    private int wins;

    // Nested classes for objects
    public static class PlaceName {
        private String _default;

        public String getDefault() { return _default; }
        public void setDefault(String _default) { this._default = _default; }
    }

    @Getter
    @Setter
    public static class TeamName {
        private String _default;
        private String fr;

        public String getDefault() { return _default; }
        public void setDefault(String _default) { this._default = _default; }
    }

    public static class TeamCommonName {
        private String _default;

        public String getDefault() { return _default; }
        public void setDefault(String _default) { this._default = _default; }
    }

    public static class TeamAbbrev {
        private String _default;

        public String getDefault() { return _default; }
        public void setDefault(String _default) { this._default = _default; }
    }
}
