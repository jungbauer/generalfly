package com.jungbauer.generalfly.service.comics;

import com.jungbauer.generalfly.domain.comics.Comic;
import com.jungbauer.generalfly.dto.comics.ComicDto;
import com.jungbauer.generalfly.repository.comics.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public ComicDto convertToDto(Comic comic) {
        ComicDto comicDto = new ComicDto();
        comicDto.id = comic.getId();
        comicDto.title = comic.getTitle();
        comicDto.linkMain = comic.getLinkMain();
        comicDto.linkAlternate = comic.getLinkAlternate();
        comicDto.useAlternateLink = comic.getUseAlternateLink();
        comicDto.chapterTotal = comic.getChapterTotal();
        comicDto.chapterCurrent = comic.getChapterCurrent();
        comicDto.notes = comic.getNotes();

        return comicDto;
    }

    public ComicDto getComicDto(Integer id) {
        if (id == null) {
            return new ComicDto();
        }

        Optional<Comic> optionalComic = comicRepository.findById(id);
        if (optionalComic.isPresent()) {
            return convertToDto(optionalComic.get());
        } else {
            return new ComicDto();
        }
    }

    public Comic updateWithDto(ComicDto comicDto, Comic comic) {
        comic.setTitle(comicDto.getTitle());
        comic.setLinkMain(comicDto.getLinkMain());
        comic.setLinkAlternate(comicDto.getLinkAlternate());
        comic.setUseAlternateLink(comicDto.getUseAlternateLink());
        comic.setChapterTotal(comicDto.getChapterTotal());
        comic.setChapterCurrent(comicDto.getChapterCurrent());
        comic.setNotes(comicDto.getNotes());

        return comic;
    }

    public Comic getComicFromDto(ComicDto comicDto) {
        if (comicDto.id == null) {
            return updateWithDto(comicDto, new Comic());
        }

        Optional<Comic> optionalComic = comicRepository.findById(comicDto.id);
        if (optionalComic.isPresent()) {
            return updateWithDto(comicDto, optionalComic.get());
        } else {
            return updateWithDto(comicDto, new Comic());
        }
    }

    public Comic saveFromDto(ComicDto comicDto) {
        Comic comic =  getComicFromDto(comicDto);
        return comicRepository.save(comic);
    }
}
