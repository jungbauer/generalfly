package com.jungbauer.generalfly.repository.comics;

import com.jungbauer.generalfly.domain.comics.Comic;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ComicRepository extends CrudRepository<Comic, Integer> {
    Optional<Comic> findByTitleAndLinkMain(String title, String linkMain);
}
