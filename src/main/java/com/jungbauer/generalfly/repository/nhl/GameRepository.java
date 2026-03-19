package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findByNhlGameId(Long nhlGameId);

    List<Game> findByGameDateBetween(LocalDate startDate, LocalDate endDate);
}
