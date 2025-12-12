package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import com.jungbauer.generalfly.domain.nhl.Team;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.repository.nhl.GameRepository;
import com.jungbauer.generalfly.repository.nhl.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;import java.util.List;

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

    // todo throwing an exception here is bad, should handle it here
    // todo this works for current season but might break for older seasons, coz nextDate might continue into new season
    public int collectInitialData() throws InterruptedException {
        System.out.println("======= collect initial data =======");
        String nextStartDate = "2025-09-20"; // preseason start for 2025-2026 season
        int totalGames = 0;
        int loop = 0;

        do {
            ScheduleDate scheduleData = nhlApiService.getScheduleByDate(nextStartDate);
            System.out.println("-----" + loop + ": between " + nextStartDate + " and " + scheduleData.getNextStartDate() + " : " + scheduleData.getNumberOfGames() + " games");
            nextStartDate = scheduleData.getNextStartDate(); // update loop variable

            // save game data
            saveFromGamesWeek(scheduleData.getGameWeek());
            // pause for the api
            Thread.sleep(2000);

            totalGames += scheduleData.getNumberOfGames();
            loop++;
        } while (nextStartDate != null);

        System.out.println("======= added " + totalGames + " games =======");

        return totalGames;
    }

    private void saveFromGamesWeek(List<ScheduleDate.GameDay> gameWeekData) {
        for (ScheduleDate.GameDay day : gameWeekData) {
            // expecting an ISO yyyy-MM-dd string
            LocalDate gameDate = LocalDate.parse(day.getDate());
            for (ScheduleDate.Game game : day.getGames()) {
                // try to find the game
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
