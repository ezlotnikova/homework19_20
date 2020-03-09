package com.gmail.ezlotnikova.web.controller;

import java.util.List;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String showItems(Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        return "items";
    }

}