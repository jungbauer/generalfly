package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Integer> {
    Season findByNhlId(Integer nhlId);
}
