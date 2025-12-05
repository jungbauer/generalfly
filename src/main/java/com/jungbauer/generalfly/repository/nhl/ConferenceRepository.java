package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Integer> {
    Conference findByNameAndAbbrev(String name, String abbrev);
}
