package com.jungbauer.generalfly.controller.comics;

import com.jungbauer.generalfly.domain.comics.Comic;
import com.jungbauer.generalfly.repository.comics.ComicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/comics")
public class ComicController {

    private final ComicRepository comicRepository;

    public ComicController(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("comics", comicRepository.findAll());
        return "comics/index";
    }

    @GetMapping("/form")
    public String comicForm(Model model) {
        model.addAttribute("comic", new Comic());
        return "comics/form";
    }

    @PostMapping("/submit")
    public String comicSubmit(@ModelAttribute Comic comic, Model model) {
        System.out.println("=========== submit comic " + comic.getId() +" ==========");
        Comic savedComic = comicRepository.save(comic);
        model.addAttribute("comic", savedComic);
        return "comics/single";
    }

    @GetMapping("/view")
    public String viewComic(@RequestParam(name = "id") String comicId, Model model) {
        Optional<Comic> viewComic = comicRepository.findById(Integer.parseInt(comicId));
        model.addAttribute("comic", viewComic.orElse(null));

        return "comics/single";
    }

    @GetMapping("/edit")
    public String editComic(@RequestParam(name = "id") String comicId, Model model) {
        Optional<Comic> editComic = comicRepository.findById(Integer.parseInt(comicId));
        if (editComic.isPresent()) {
            model.addAttribute("comic", editComic.orElse(null));
            return "comics/form";
        } else {
            // todo make proper error page
            return "/error";
        }
    }

    @GetMapping("/increment")
    public String incrementComic(@RequestParam(name = "id") String comicId, Model model) {
        Optional<Comic> optionalComicComic = comicRepository.findById(Integer.parseInt(comicId));
        if (optionalComicComic.isPresent()) {
            Comic comic = optionalComicComic.get();
            comic.incrementChapterCurrent();
            Comic savedComic = comicRepository.save(comic);
            model.addAttribute("comic", savedComic);
            return "comics/single";
        } else {
            // todo make proper error page
            return "/error";
        }
    }
}
