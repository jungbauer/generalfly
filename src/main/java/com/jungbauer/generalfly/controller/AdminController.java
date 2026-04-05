package com.jungbauer.generalfly.controller;

import com.jungbauer.generalfly.domain.nhl.Season;
import com.jungbauer.generalfly.repository.nhl.SeasonRepository;
import com.jungbauer.generalfly.service.nhl.GameUpdateService;
import com.jungbauer.generalfly.service.nhl.NhlDataService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminController {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final SeasonRepository seasonRepository;
    private final NhlDataService nhlDataService;
    private final GameUpdateService gameUpdateService;

    public AdminController(SeasonRepository seasonRepository, NhlDataService nhlDataService, GameUpdateService gameUpdateService) {
        this.seasonRepository = seasonRepository;
        this.nhlDataService = nhlDataService;
        this.gameUpdateService = gameUpdateService;
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

    @PostMapping("/admin/nhl-data/update-games")
    public String updateGamesForDate(@RequestParam("date") LocalDate date,
                                     RedirectAttributes redirectAttributes) {
        GameUpdateService.UpdateResult result = gameUpdateService.updateGamesForDate(date);

        String message;
        if (result.isSuccess()) {
            message = String.format("Successfully updated games for %s: checked %d games, updated %d games between %s and %s",
                    date, result.getGamesChecked(), result.getGamesUpdated(),
                    result.getOldestDate().format(dateFormatter), result.getMostRecentDate().format(dateFormatter));
        } else {
            message = String.format("Failed to update games for %s: %s",
                    date, result.getErrorMessage());
        }

        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/admin/nhl-data";
    }

    @GetMapping("/admin/popseasons")
    public ResponseEntity<String> populateNhlSeasons() {
        return ResponseEntity.ok().body(nhlDataService.populateNhlSeasons());
    }

    @GetMapping("/admin/popdivconf")
    public ResponseEntity<String> populateDivisionAndConference() {
        return ResponseEntity.ok().body(nhlDataService.populateDivisionAndConference());
    }
}
