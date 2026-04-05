package com.jungbauer.generalfly.repository.nhl;

import com.jungbauer.generalfly.domain.nhl.Division;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository  extends JpaRepository<Division, Integer> {
    Division findByNameAndAbbrev(String name, String abbrev);
}
