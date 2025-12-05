package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findByNhlGameId(Long nhlGameId);
}
