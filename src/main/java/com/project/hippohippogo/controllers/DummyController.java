package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.DummyItem;
import com.project.hippohippogo.exceptions.DummyResourceNotFoundException;
import com.project.hippohippogo.repositories.DummyRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class DummyController {

    private DummyRepository dummyRepository;

    @Autowired
    public void setDummyRepository(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    @GetMapping("/dummy")
    public String getAllItems(Model model) {
        List<DummyItem> items = (List<DummyItem>) dummyRepository.findAll();
//        System.out.print(items);
        model.addAttribute("items", items);
        return "showDummyData";
    }

    @PostMapping("/dummy")
    public DummyItem createItem(@Valid @RequestBody DummyItem dummyItem) {
        return dummyRepository.save(dummyItem);
    }

    @GetMapping("/dummy/{id}")
    public Optional<DummyItem> getItemById(@PathVariable(value = "id") Long dummyItemId) {
        return dummyRepository.findById(dummyItemId);
    }

    @PutMapping("/dummy/{id}")
    public DummyItem updateItem(@PathVariable(value = "id") Long dummyItemId,
                                @Valid @RequestBody DummyItem dummyItemDetails) {

        DummyItem dummyItem = dummyRepository.findById(dummyItemId).orElseThrow(() -> new DummyResourceNotFoundException(dummyItemId));

        dummyItem.set_id(dummyItemDetails.get_id());
        dummyItem.setName(dummyItemDetails.getName());

        return dummyRepository.save(dummyItem);
    }

    @DeleteMapping("/dummy/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable(value = "id") Long dummyItemId) {
        DummyItem dummyItem = dummyRepository.findById(dummyItemId).orElseThrow(() -> new DummyResourceNotFoundException(dummyItemId));

        dummyRepository.delete(dummyItem);

        return ResponseEntity.ok().build();
    }
}