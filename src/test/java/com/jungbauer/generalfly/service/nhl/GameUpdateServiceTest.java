package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.repository.nhl.GameRepository;
import com.jungbauer.generalfly.service.DumpLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameUpdateServiceTest {

    @Mock
    private NhlApiService nhlApiService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private DumpLogService dumpLogService;

    private GameUpdateService gameUpdateService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        gameUpdateService = new GameUpdateService(nhlApiService, gameRepository, dumpLogService);
    }

    @Test
    void updateRecentGames_callsApiForThreeDays() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);
        String yesterdayStr = today.minusDays(1).format(dateFormatter);
        String twoDaysAgoStr = today.minusDays(2).format(dateFormatter);

        ScheduleDate emptySchedule = createEmptySchedule();

        when(nhlApiService.getScheduleByDate(todayStr)).thenReturn(emptySchedule);
        when(nhlApiService.getScheduleByDate(yesterdayStr)).thenReturn(emptySchedule);
        when(nhlApiService.getScheduleByDate(twoDaysAgoStr)).thenReturn(emptySchedule);

        gameUpdateService.updateRecentGames();

        verify(nhlApiService, times(1)).getScheduleByDate(todayStr);
        verify(nhlApiService, times(1)).getScheduleByDate(yesterdayStr);
        verify(nhlApiService, times(1)).getScheduleByDate(twoDaysAgoStr);
    }

    @Test
    void updateRecentGames_updatesGameWhenDataDiffers() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);
        String minus1Str = today.minusDays(1).format(dateFormatter);
        String minus2Str = today.minusDays(2).format(dateFormatter);

        ScheduleDate.Game apiGame1 = createApiGame(1L, "OFF", "REG", 3, 2);
        ScheduleDate.Game apiGame2 = createApiGame(2L, "OFF", "OT", 4, 5);
        ScheduleDate.Game apiGame3 = createApiGame(3L, "FUT", null, null, null);
        ScheduleDate scheduleDate1 = createScheduleWithGame(todayStr, apiGame1);
        ScheduleDate scheduleDate2 = createScheduleWithGame(minus1Str, apiGame2);
        ScheduleDate scheduleDate3 = createScheduleWithGame(minus2Str, apiGame3);

        Game dbGame = createDbGame(1L, "LIVE", null, null, null);
        Game dbGame2 = createDbGame(2L, "FUT", null, null, null);
        Game dbGame3 = createDbGame(3L, "FUT", null, null, null);

        when(nhlApiService.getScheduleByDate(todayStr)).thenReturn(scheduleDate1);
        when(nhlApiService.getScheduleByDate(minus1Str)).thenReturn(scheduleDate2);
        when(nhlApiService.getScheduleByDate(minus2Str)).thenReturn(scheduleDate3);

        when(gameRepository.findByNhlGameId(1L)).thenReturn(dbGame);
        when(gameRepository.findByNhlGameId(2L)).thenReturn(dbGame2);
        when(gameRepository.findByNhlGameId(3L)).thenReturn(dbGame3);

        when(gameRepository.save(any(Game.class))).thenReturn(dbGame);

        gameUpdateService.updateRecentGames();

        verify(gameRepository, times(1)).save(dbGame);
    }

    @Test
    void updateRecentGames_doesNotUpdateWhenDataIsSame() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);

        ScheduleDate.Game apiGame = createApiGame(1L, "OFF", "REG", 3, 2);
        ScheduleDate scheduleDate = createScheduleWithGame(apiGame);

        Game dbGame = createDbGame(1L, "OFF", "REG", 3, 2);

        when(nhlApiService.getScheduleByDate(todayStr)).thenReturn(scheduleDate);
        when(gameRepository.findByNhlGameId(1L)).thenReturn(dbGame);

        gameUpdateService.updateRecentGames();

        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void updateRecentGames_skipsFutureGamesWithoutScores() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);

        ScheduleDate.Game apiGame = createApiGame(1L, "FUT", null, null, null);
        ScheduleDate scheduleDate = createScheduleWithGame(apiGame);

        Game dbGame = createDbGame(1L, "FUT", null, null, null);

        when(nhlApiService.getScheduleByDate(todayStr)).thenReturn(scheduleDate);
        when(gameRepository.findByNhlGameId(1L)).thenReturn(dbGame);

        gameUpdateService.updateRecentGames();

        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void updateRecentGames_skipsNonExistentGames() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);

        ScheduleDate.Game apiGame = createApiGame(999L, "OFF", "REG", 3, 2);
        ScheduleDate scheduleDate = createScheduleWithGame(apiGame);

        when(nhlApiService.getScheduleByDate(todayStr)).thenReturn(scheduleDate);
        when(gameRepository.findByNhlGameId(999L)).thenReturn(null);

        gameUpdateService.updateRecentGames();

        verify(gameRepository, never()).save(any(Game.class));
    }

    private ScheduleDate createEmptySchedule() {
        ScheduleDate scheduleDate = new ScheduleDate();
        scheduleDate.setGameWeek(new ArrayList<>());
        return scheduleDate;
    }

    private ScheduleDate createScheduleWithGame(ScheduleDate.Game apiGame) {
        ScheduleDate.GameDay gameDay = new ScheduleDate.GameDay();
        gameDay.setDate(LocalDate.now().toString());
        gameDay.setGames(List.of(apiGame));

        ScheduleDate scheduleDate = new ScheduleDate();
        scheduleDate.setGameWeek(List.of(gameDay));
        return scheduleDate;
    }

    private ScheduleDate createScheduleWithGame(String dateStr, ScheduleDate.Game apiGame) {
        ScheduleDate.GameDay gameDay = new ScheduleDate.GameDay();
        gameDay.setDate(dateStr);
        gameDay.setGames(List.of(apiGame));

        ScheduleDate scheduleDate = new ScheduleDate();
        scheduleDate.setGameWeek(List.of(gameDay));
        return scheduleDate;
    }

    private ScheduleDate.Game createApiGame(Long id, String gameState, String lastPeriodType,
                                               Integer homeScore, Integer awayScore) {
        ScheduleDate.Game game = new ScheduleDate.Game();
        game.setId(id);
        game.setGameState(gameState);

        if (lastPeriodType != null) {
            ScheduleDate.GameOutcome outcome = new ScheduleDate.GameOutcome();
            outcome.setLastPeriodType(lastPeriodType);
            game.setGameOutcome(outcome);
        }

        if (homeScore != null) {
            ScheduleDate.Team homeTeam = new ScheduleDate.Team();
            homeTeam.setScore(homeScore);
            game.setHomeTeam(homeTeam);
        }

        if (awayScore != null) {
            ScheduleDate.Team awayTeam = new ScheduleDate.Team();
            awayTeam.setScore(awayScore);
            game.setAwayTeam(awayTeam);
        }

        return game;
    }

    private Game createDbGame(Long nhlGameId, String gameState, String gameOutcome,
                               Integer homeScore, Integer awayScore) {
        Game game = new Game();
        game.setNhlGameId(nhlGameId);
        game.setGameState(gameState);
        game.setGameOutcome(gameOutcome);
        game.setHomeTeamScore(homeScore);
        game.setAwayTeamScore(awayScore);
        return game;
    }
}
