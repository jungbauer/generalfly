package com.jungbauer.generalfly.dto.nhl.uiapp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GameView {
    private Long nhlGameId;
    private Long season;
    private Integer gameType;
    private String gameState;
    private String gameOutcome;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private LocalDate gameDate;
    private TeamView awayTeam;
    private TeamView homeTeam;

    public GameView(com.jungbauer.generalfly.domain.nhl.Game game) {
        this.nhlGameId = game.getNhlGameId();
        this.season = game.getSeason();
        this.gameType = game.getGameType();
        this.gameState = game.getGameState();
        this.gameOutcome = game.getGameOutcome();
        this.homeTeamScore = game.getHomeTeamScore();
        this.awayTeamScore = game.getAwayTeamScore();
        this.gameDate = game.getGameDate();
        if (game.getAwayTeam() != null) {
            this.awayTeam = new TeamView(
                game.getAwayTeam().getNhlId(),
                game.getAwayTeam().getCommonName(),
                game.getAwayTeam().getPlaceName(),
                game.getAwayTeam().getAbbrev(),
                game.getAwayTeam().getLogo(),
                game.getAwayTeam().getDarkLogo()
            );
        }
        if (game.getHomeTeam() != null) {
            this.homeTeam = new TeamView(
                game.getHomeTeam().getNhlId(),
                game.getHomeTeam().getCommonName(),
                game.getHomeTeam().getPlaceName(),
                game.getHomeTeam().getAbbrev(),
                game.getHomeTeam().getLogo(),
                game.getHomeTeam().getDarkLogo()
            );
        }
    }
}
