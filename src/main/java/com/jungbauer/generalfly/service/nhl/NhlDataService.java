package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import com.jungbauer.generalfly.domain.nhl.Team;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.repository.nhl.GameRepository;
import com.jungbauer.generalfly.repository.nhl.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NhlDataService {

    private final NhlApiService nhlApiService;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    public NhlDataService(NhlApiService nhlApiService, TeamRepository teamRepository,
                          GameRepository gameRepository) {
        this.nhlApiService = nhlApiService;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    public void collectGameData(String date) {
        ScheduleDate data = nhlApiService.getScheduleByDate(date);
        for (ScheduleDate.GameDay day : data.getGameWeek()) {
            // expecting an ISO yyyy-MM-dd string
            LocalDate gameDate = LocalDate.parse(day.getDate());
            for (ScheduleDate.Game game : day.getGames()) {
                // try find the game
                Game dbGame = gameRepository.findByNhlGameId(game.getId());
                if (dbGame == null) {
                    // save new game
                    Game newGame = new Game();
                    newGame.setNhlGameId(game.getId());
                    newGame.setSeason(game.getSeason());
                    newGame.setGameDate(gameDate);
                    newGame.setGameType(game.getGameType());
                    newGame.setGameState(game.getGameState());
                    newGame.setHomeTeam(getAndSaveTeam(game.getHomeTeam()));
                    newGame.setAwayTeam(getAndSaveTeam(game.getAwayTeam()));

                    // Future games, FUT, do not have an outcome or scores yet
                    if (!game.getGameState().equals("FUT")) {
                        newGame.setGameOutcome(game.getGameOutcome().getLastPeriodType());
                        newGame.setHomeTeamScore(game.getHomeTeam().getScore());
                        newGame.setAwayTeamScore(game.getAwayTeam().getScore());
                    }

                    gameRepository.save(newGame);
                }
            }
        }
    }


    /**
     * Method tries to retrieve a team from the database, if no team is found it creates the entry.
     * @param dtoTeam A team object from the api
     * @return either a found database team or the newly created database team
     */
    private Team getAndSaveTeam(ScheduleDate.Team dtoTeam) {
        Team dbTeam = teamRepository.findByNhlId(dtoTeam.getId());
        if (dbTeam == null) {
            // time to save a new team
            Team newTeam = new Team();
            newTeam.setNhlId(dtoTeam.getId());
            newTeam.setAbbrev(dtoTeam.getAbbrev());
            newTeam.setLogo(dtoTeam.getLogo());
            newTeam.setDarkLogo(dtoTeam.getDarkLogo());
            newTeam.setCommonName(dtoTeam.getCommonName().get("default"));
            newTeam.setPlaceName(dtoTeam.getPlaceName().get("default"));

            return teamRepository.save(newTeam);
        }
        return dbTeam;
    }
}
