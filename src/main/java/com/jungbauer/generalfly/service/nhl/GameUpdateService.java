package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import com.jungbauer.generalfly.dto.nhl.api.ScheduleDate;
import com.jungbauer.generalfly.repository.nhl.GameRepository;
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

    @Scheduled(cron = "0 0 4 * * ?")
    public void updateRecentGames() {
        log.info("Starting scheduled game update process");

        LocalDate today = LocalDate.now();
        int totalChecked = 0;
        int totalUpdated = 0;

        for (int daysAgo = 0; daysAgo < 3; daysAgo++) {
            LocalDate date = today.minusDays(daysAgo);
            String formattedDate = date.format(dateFormatter);

            try {
                ScheduleDate scheduleDate = nhlApiService.getScheduleByDate(formattedDate);

                if (scheduleDate.getGameWeek() == null) {
                    log.debug("No games found for date: {}", formattedDate);
                    continue;
                }

                int dayChecked = 0;
                int dayUpdated = 0;

                for (ScheduleDate.GameDay gameDay : scheduleDate.getGameWeek()) {
                    if (gameDay.getGames() == null) {
                        continue;
                    }

                    for (ScheduleDate.Game apiGame : gameDay.getGames()) {
                        dayChecked++;
                        boolean updated = updateGameIfChanged(apiGame);
                        if (updated) {
                            dayUpdated++;
                        }
                    }
                }

                log.info("Date {}: checked {} games, updated {} games", formattedDate, dayChecked, dayUpdated);
                totalChecked += dayChecked;
                totalUpdated += dayUpdated;

            } catch (Exception e) {
                log.error("Error updating games for date {}: {}", formattedDate, e.getMessage(), e);
            }
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
