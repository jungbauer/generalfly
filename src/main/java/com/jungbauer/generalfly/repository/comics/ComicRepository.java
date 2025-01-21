package com.jungbauer.generalfly.repository.comics;

import com.jungbauer.generalfly.domain.comics.Comic;
import org.springframework.data.repository.CrudRepository;

public interface ComicRepository extends CrudRepository<Comic, Integer> {
}
