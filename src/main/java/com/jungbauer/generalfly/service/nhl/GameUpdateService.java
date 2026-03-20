package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.repository.nhl.GameRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class GameUpdateService {

    private final NhlApiService nhlApiService;
    private final GameRepository gameRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GameUpdateService(NhlApiService nhlApiService, GameRepository gameRepository) {
        this.nhlApiService = nhlApiService;
        this.gameRepository = gameRepository;
    }

    @Getter
    public static class UpdateResult {
        private final LocalDate date;
        private final int gamesChecked;
        private final int gamesUpdated;
        private final String errorMessage;
        private final LocalDate oldestDate;
        private final LocalDate mostRecentDate;

        public UpdateResult(LocalDate date, int gamesChecked, int gamesUpdated, String errorMessage) {
            this.date = date;
            this.gamesChecked = gamesChecked;
            this.gamesUpdated = gamesUpdated;
            this.errorMessage = errorMessage;
            this.oldestDate = null;
            this.mostRecentDate = null;
        }

        public UpdateResult(LocalDate date, int gamesChecked, int gamesUpdated, String errorMessage,
                            LocalDate oldestDate, LocalDate mostRecentDate) {
            this.date = date;
            this.gamesChecked = gamesChecked;
            this.gamesUpdated = gamesUpdated;
            this.errorMessage = errorMessage;
            this.oldestDate = oldestDate;
            this.mostRecentDate = mostRecentDate;
        }

        public boolean isSuccess() {
            return errorMessage == null;
        }
    }

    public UpdateResult updateGamesForDate(LocalDate date) {
        String formattedDate = date.format(dateFormatter);
        LocalDate oldest = date;
        LocalDate recent = date;

        try {
            ScheduleDate scheduleDate = nhlApiService.getScheduleByDate(formattedDate);

            if (scheduleDate.getGameWeek() == null) {
                log.debug("No games found for date: {}", formattedDate);
                return new UpdateResult(date, 0, 0, null);
            }

            int gamesChecked = 0;
            int gamesUpdated = 0;

            for (ScheduleDate.GameDay gameDay : scheduleDate.getGameWeek()) {
                LocalDate gameDate = LocalDate.parse(gameDay.getDate());
                if (gameDate.isBefore(oldest)) {
                    oldest = gameDate;
                }
                if (gameDate.isAfter(recent)) {
                    recent = gameDate;
                }

                if (gameDay.getGames() == null) {
                    continue;
                }

                for (ScheduleDate.Game apiGame : gameDay.getGames()) {
                    gamesChecked++;
                    boolean updated = updateGameIfChanged(apiGame);
                    if (updated) {
                        gamesUpdated++;
                    }
                }
            }

            log.info("Date {}: checked {} games, updated {} games between {} and {}", formattedDate, gamesChecked,
                    gamesUpdated, oldest.format(dateFormatter), recent.format(dateFormatter));
            return new UpdateResult(date, gamesChecked, gamesUpdated, null, oldest, recent);

        } catch (Exception e) {
            log.error("Error updating games for date {}: {}", formattedDate, e.getMessage(), e);
            return new UpdateResult(date, 0, 0, e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void updateRecentGames() {
        log.info("Starting scheduled game update process");

        LocalDate today = LocalDate.now();
        int totalChecked = 0;
        int totalUpdated = 0;

        for (int daysAgo = 0; daysAgo < 3; daysAgo++) {
            LocalDate date = today.minusDays(daysAgo);
            UpdateResult result = updateGamesForDate(date);

            if (!result.isSuccess()) {
                log.error("Failed to update games for date {}: {}", date, result.getErrorMessage());
                continue;
            }

            totalChecked += result.getGamesChecked();
            totalUpdated += result.getGamesUpdated();
        }

        log.info("Completed game update process: checked {} games total, updated {} games", totalChecked, totalUpdated);
    }

    private boolean updateGameIfChanged(ScheduleDate.Game apiGame) {
        Game dbGame = gameRepository.findByNhlGameId(apiGame.getId());

        if (dbGame == null) {
            log.debug("Game {} not found in database, skipping update", apiGame.getId());
            return false;
        }

        boolean needsUpdate = false;

        if (!Objects.equals(dbGame.getGameState(), apiGame.getGameState())) {
            dbGame.setGameState(apiGame.getGameState());
            needsUpdate = true;
        }

        if (!"FUT".equals(apiGame.getGameState())) {
            String apiOutcome = apiGame.getGameOutcome() != null ? apiGame.getGameOutcome().getLastPeriodType() : null;
            Integer apiHomeScore = apiGame.getHomeTeam() != null ? apiGame.getHomeTeam().getScore() : null;
            Integer apiAwayScore = apiGame.getAwayTeam() != null ? apiGame.getAwayTeam().getScore() : null;

            if (!Objects.equals(dbGame.getGameOutcome(), apiOutcome)) {
                dbGame.setGameOutcome(apiOutcome);
                needsUpdate = true;
            }

            if (!Objects.equals(dbGame.getHomeTeamScore(), apiHomeScore)) {
                dbGame.setHomeTeamScore(apiHomeScore);
                needsUpdate = true;
            }

            if (!Objects.equals(dbGame.getAwayTeamScore(), apiAwayScore)) {
                dbGame.setAwayTeamScore(apiAwayScore);
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            gameRepository.save(dbGame);
            log.debug("Updated game {} (ID: {})", apiGame.getId(), dbGame.getId());
        }

        return needsUpdate;
    }
}
