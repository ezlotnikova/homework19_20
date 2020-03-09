package com.gmail.ezlotnikova.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gmail.ezlotnikova.service.model.constant.ItemStatusEnum;

public class ItemDTO {

    private Long id;
    @NotNull(message = "Item Name must not be empty")
    @Size(min = 5, max = 40, message = "Item Name must be between 5 and 40 characters long")
    private String name;
    @NotNull(message = "Status must not be empty")
    private ItemStatusEnum status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ItemStatusEnum status) {
        this.status = status;
    }

}