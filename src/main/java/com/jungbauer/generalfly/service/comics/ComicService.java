package com.jungbauer.generalfly.service.comics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jungbauer.generalfly.domain.User;
import com.jungbauer.generalfly.domain.comics.Comic;
import com.jungbauer.generalfly.dto.comics.ComicDto;
import com.jungbauer.generalfly.repository.comics.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComicService {

    private final ComicRepository comicRepository;
    private final ObjectMapper objectMapper;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
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

    public Comic getComicFromDto(ComicDto comicDto, User user) {
        if (comicDto.id == null) {
            return updateWithDto(comicDto, new Comic());
        }

        Optional<Comic> optionalComic = comicRepository.findByUserIdAndId(user.getId(), comicDto.id);
        if (optionalComic.isPresent()) {
            return updateWithDto(comicDto, optionalComic.get());
        } else {
            return updateWithDto(comicDto, new Comic());
        }
    }

    public Comic saveFromDto(ComicDto comicDto, User user) {
        Comic comic =  getComicFromDto(comicDto, user);
        comic.setUser(user);
        return comicRepository.save(comic);
    }

    public List<ComicDto> getComicsFromJson(String json) throws JsonProcessingException {
        List<ComicDto> comicList = objectMapper.readValue(json, new TypeReference<List<ComicDto>>() {});
        for (ComicDto comic : comicList) {
            System.out.printf("Comic:%d %s %s%n", comic.id, comic.title, comic.linkMain);
        }
        System.out.println("Comics from json: " + comicList.size());
        return comicList;
    }

    public int importComicsFromJson(String json, User user) throws JsonProcessingException {
        List<ComicDto> comicList = getComicsFromJson(json);
        int count = 0;
        for (ComicDto comic : comicList) {
            Optional<Comic> optionalComic = comicRepository.findByUserIdAndTitleAndLinkMain(user.getId(), comic.title, comic.linkMain);
            if (optionalComic.isEmpty()) {
                saveFromDto(comic, user);
                count++;
            } else {
                System.out.println("Duplicate comic: " + comic.title + ", " + comic.linkMain);
            }
        }
        return count;
    }

    public String getAllComicsJson(User user) throws JsonProcessingException {
        List<Comic> comics = comicRepository.findAllByUserId(user.getId());
        List<ComicDto> dtoList = new ArrayList<>();
        for (Comic comic : comics) {
            dtoList.add(convertToDto(comic));
        }

        return objectMapper.writeValueAsString(dtoList);
    }
}
