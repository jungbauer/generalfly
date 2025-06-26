package com.jungbauer.generalfly.repository.comics;

import com.jungbauer.generalfly.domain.comics.Comic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ComicRepository extends CrudRepository<Comic, Integer> {
    List<Comic> findAllByUserId(Long userId);
    Optional<Comic> findByUserIdAndId(Long userId, Integer comicId);
    Optional<Comic> findByUserIdAndTitleAndLinkMain(Long userId, String title, String linkMain);
}
