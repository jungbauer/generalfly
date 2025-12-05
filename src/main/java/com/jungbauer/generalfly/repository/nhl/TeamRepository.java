package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByNhlId(Integer nhlId);
    Team findByAbbrev(String abbrev);
}
