package com.jungbauer.generalfly.controller;

import com.jungbauer.generalfly.domain.DumpLog;
import com.jungbauer.generalfly.repository.DumpLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DumpLogController {

    private final DumpLogRepository dumpLogRepository;

    public DumpLogController(DumpLogRepository dumpLogRepository) {
        this.dumpLogRepository = dumpLogRepository;
    }

    @GetMapping("/logs")
    public String showLogs(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<DumpLog> logPage = dumpLogRepository.findAll(
                PageRequest.of(page, 10, Sort.by("createdOn").descending())
        );

        model.addAttribute("logs", logPage.getContent());
        model.addAttribute("currentPage", logPage.getNumber());
        model.addAttribute("totalPages", logPage.getTotalPages());
        model.addAttribute("totalItems", logPage.getTotalElements());

        return "logs/index";
    }
}
