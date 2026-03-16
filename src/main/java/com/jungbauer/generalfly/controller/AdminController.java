package com.jungbauer.generalfly.controller;

import com.jungbauer.generalfly.domain.nhl.Season;
import com.jungbauer.generalfly.repository.nhl.SeasonRepository;
import com.jungbauer.generalfly.service.nhl.NhlDataService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private final SeasonRepository seasonRepository;
    private final NhlDataService nhlDataService;

    public AdminController(SeasonRepository seasonRepository, NhlDataService nhlDataService) {
        this.seasonRepository = seasonRepository;
        this.nhlDataService = nhlDataService;
    }

    @GetMapping("/admin/nhl-data")
    public String nhlDataPage(Model model) {
        List<Season> seasons = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "seasonOrdinal"));
        model.addAttribute("seasons", seasons);
        return "admin/nhl-data";
    }

    @PostMapping("/admin/nhl-data/collect")
    public String collectSeasonData(@RequestParam("seasonId") Integer seasonId,
                                    RedirectAttributes redirectAttributes) {
        String message = nhlDataService.collectSeasonData(seasonId);
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/admin/nhl-data";
    }
}
