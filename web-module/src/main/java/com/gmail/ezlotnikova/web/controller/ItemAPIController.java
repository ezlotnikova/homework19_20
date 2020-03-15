package com.gmail.ezlotnikova.web.controller;

import java.util.List;
import java.util.stream.Stream;
import javax.validation.Valid;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.constant.ItemStatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ItemAPIController {

    private final ItemService itemService;

    public ItemAPIController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<ItemDTO> showAllItems() {
        return itemService.findAll();
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(
            @Valid @RequestBody ItemDTO itemDTO) {
        return itemService.add(itemDTO);
    }

    @PutMapping("/items/{id}/{status}")
    public ResponseEntity<Object> updateStatusById(@PathVariable Long id, @PathVariable @Valid String status) {
        if (statusValid(status)) {
            int rowsUpdated = itemService.updateStatusById(id, status);
            return new ResponseEntity<>(rowsUpdated + " item(s) updated to status: "
                    + status.toUpperCase(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Status " + status.toUpperCase() +
                    " is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/items/{status}")
    public ResponseEntity<Object> deleteItemsByStatus(@PathVariable String status) {
        if (statusValid(status)) {
            int rowsDeleted = itemService.deleteItemsByStatus(status.toUpperCase());
            return new ResponseEntity<>(
                    rowsDeleted + " item(s) with status " + status.toUpperCase() + " deleted",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Status " + status.toUpperCase() +
                    " is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean statusValid(String status) {
        return Stream.of(ItemStatusEnum.values())
                .anyMatch(itemStatusEnum -> itemStatusEnum.name().equals(status.toUpperCase()));
    }

}