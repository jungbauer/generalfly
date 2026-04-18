package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.*;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.dto.nhl.api.Seasons;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.dto.nhl.api.StandingsTeam;
import com.jungbauer.generalfly.dto.nhl.uiapp.GameView;
import com.jungbauer.generalfly.dto.nhl.uiapp.GamesAroundToday;
import com.jungbauer.generalfly.dto.nhl.uiapp.SeasonGames;
import com.jungbauer.generalfly.dto.nhl.uiapp.SeasonView;
import com.jungbauer.generalfly.repository.nhl.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NhlDataService {

    private final NhlApiService nhlApiService;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final ConferenceRepository conferenceRepository;
    private final DivisionRepository divisionRepository;
    private final SeasonRepository seasonRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NhlDataService(NhlApiService nhlApiService, TeamRepository teamRepository,
                          GameRepository gameRepository, ConferenceRepository conferenceRepository,
                          DivisionRepository divisionRepository, SeasonRepository seasonRepository) {
        this.nhlApiService = nhlApiService;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.conferenceRepository = conferenceRepository;
        this.divisionRepository = divisionRepository;
        this.seasonRepository = seasonRepository;
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

    /**
     * This populates team db entries with their conference and division.
     * We assume teams already have db entries.
     */
    public String  populateDivisionAndConference() {
        Standings standings = nhlApiService.getStandingsNow();
        for (StandingsTeam apiTeam : standings.getStandings()) {
            String conferenceName = apiTeam.getConferenceName();
            String conferenceAbbrev = apiTeam.getConferenceAbbrev();
            String divisionName = apiTeam.getDivisionName();
            String divisionAbbrev = apiTeam.getDivisionAbbrev();

            // standings data does NOT include the NHL id, thus using abbreviation.
            Team dbTeam = teamRepository.findByAbbrev(apiTeam.getTeamAbbrev().getDefault());
            boolean updateTeam = dbTeam.getConference() == null || dbTeam.getDivision() == null;

            Conference conference = conferenceRepository.findByNameAndAbbrev(conferenceName, conferenceAbbrev);
            if (conference == null) {
                conference = conferenceRepository.save(new Conference(conferenceAbbrev, conferenceName));
            }

            Division division = divisionRepository.findByNameAndAbbrev(divisionName, divisionAbbrev);
            if (division == null) {
                division = divisionRepository.save(new Division(divisionAbbrev, divisionName));
            }

            if (updateTeam) {
                dbTeam.setConference(conference);
                dbTeam.setDivision(division);
                teamRepository.save(dbTeam);
            }
        }
        return "Populated divisions and conferences";
    }

    public String populateNhlSeasons() {
        int savedTotal = 0;
        Seasons detailedSeasons = nhlApiService.getDetailedSeasons();
        for (Seasons.SeasonDetails detailedSeason : detailedSeasons.getData()) {
            Season dbSeason = seasonRepository.findByNhlId(detailedSeason.getId());
            if (dbSeason == null) {
                dbSeason = seasonRepository.save(convertApiToDb(detailedSeason));
                savedTotal++;
            }
        }

        return "Total seasons saved: " + savedTotal;
    }

    private Season convertApiToDb(Seasons.SeasonDetails apiSeason) {
        Season dbSeason = new Season();
        dbSeason.setNhlId(apiSeason.getId());
        dbSeason.setFormattedSeasonId(apiSeason.getFormattedSeasonId());
        dbSeason.setSeasonOrdinal(apiSeason.getSeasonOrdinal());

        if (apiSeason.getStartDate() != null) {
            dbSeason.setStartDate(apiSeason.getStartDate().toLocalDate());
        }
        if (apiSeason.getEndDate() != null) {
            dbSeason.setEndDate(apiSeason.getEndDate().toLocalDate());
        }
        if (apiSeason.getPreseasonStartdate() != null) {
            dbSeason.setPreseasonStartDate(apiSeason.getPreseasonStartdate().toLocalDate());
        }
        if (apiSeason.getRegularSeasonEndDate() != null) {
            dbSeason.setRegularSeasonEndDate(apiSeason.getRegularSeasonEndDate().toLocalDate());
        }

        return dbSeason;
    }

    // todo this works for current season but might break for older seasons, coz nextDate might continue into new season
    public String collectSeasonData(Integer seasonId) {
        System.out.println("Collecting data for season: " + seasonId);
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found: " + seasonId));

        String nextStartDate = season.getStartDate().format(dateFormatter);

        if (season.getPreseasonStartDate() != null) {
            if (season.getPreseasonStartDate().isBefore(season.getStartDate())) {
                nextStartDate = season.getPreseasonStartDate().format(dateFormatter);
            }
        }

        int totalGames = 0;
        int loop = 0;

        try {
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

            season.setCollected(true);
            seasonRepository.save(season);
        }
        catch (InterruptedException interruptedException) {
            System.out.println("ERROR interruptedException: " + totalGames + " games before interrupt.");
        }


        System.out.println("======= added " + totalGames + " games =======");

        return "Collected data for season: " + season.getFormattedSeasonId() + ", saved games: " + totalGames;
    }

    public GamesAroundToday getGamesAroundToday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        List<Game> games = gameRepository.findByGameDateBetween(yesterday, tomorrow);

        GamesAroundToday result = new GamesAroundToday();
        result.setYesterdayDate(yesterday.format(dateFormatter));
        result.setTodayDate(today.format(dateFormatter));
        result.setTomorrowDate(tomorrow.format(dateFormatter));

        for (Game game : games) {
            GameView gameView = new GameView(game);
            if (yesterday.equals(game.getGameDate())) {
                result.addYesterday(gameView);
            } else if (today.equals(game.getGameDate())) {
                result.addToday(gameView);
            } else if (tomorrow.equals(game.getGameDate())) {
                result.addTomorrow(gameView);
            }
        }

        return result;
    }

    public List<SeasonView> getDbSeasons() {
        List<Season> dbSeasons = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
        List<SeasonView> apiSeasons = new ArrayList<>();
        for (Season dbSeason : dbSeasons) {
            SeasonView apiSeason = new SeasonView(dbSeason);
            apiSeasons.add(apiSeason);
        }

        return apiSeasons;
    }

    public SeasonGames getSeasonGames(String seasonStr) {
        Season dbSeason = seasonRepository.findByNhlId(Integer.valueOf(seasonStr));
        //todo need to throw an error here if the season is unrecognised or uncollected

        List<Game> dbGames = gameRepository.findGamesBySeason(dbSeason.getNhlId().longValue());

        SeasonGames seasonGames = new SeasonGames();
        seasonGames.setSeason(new SeasonView(dbSeason));

        List<GameView> apiGames = new ArrayList<>();
        for (Game dbGame : dbGames) {
            apiGames.add(new GameView(dbGame));
        }

        seasonGames.setGames(apiGames);

        return seasonGames;
    }

    private String getCurrentSeasonStr(LocalDate currentDate, List<Season> seasons) {
        int i = 0;
        String currentSeason = "20252026";

        // Comparison done in pairs. B is the older season.
        do {
            Season testSeasonA = seasons.get(i);
            Season testSeasonB = seasons.get(i+1);
            LocalDate testDateA = testSeasonA.getPreseasonStartDate() != null ? testSeasonA.getPreseasonStartDate() : testSeasonA.getStartDate();
            LocalDate testDateB = testSeasonB.getPreseasonStartDate() != null ? testSeasonB.getPreseasonStartDate() : testSeasonB.getStartDate();

            if (currentDate.isEqual(testDateA) || currentDate.isAfter(testDateA)) {
                currentSeason = String.valueOf(testSeasonA.getNhlId());
                break;
            }
            if (currentDate.isEqual(testDateB) || (currentDate.isAfter(testDateB) && currentDate.isBefore(testDateA))) {
                currentSeason = String.valueOf(testSeasonB.getNhlId());
                break;
            }

            i++;
        } while(i < (seasons.size() - 1));

        return currentSeason;
    }

    private String getCurrentSeasonStr() {
        List<Season> seasons = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
        LocalDate currentDate = LocalDate.now();
        return getCurrentSeasonStr(currentDate, seasons);
    }
}
