package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.DummyItem;
import com.project.hippohippogo.repositories.DummyRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchController {

    private DummyRepository dummyRepository;

    @Autowired
    public void setDummyRepository(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    @GetMapping("/search")
    public String getTxtResults(Model model) {
        var items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }

    @GetMapping("/img")
    public String getImgResults(Model model) {
        var items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }
}