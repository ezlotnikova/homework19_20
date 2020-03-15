package com.gmail.ezlotnikova.service;

import java.util.List;

import com.gmail.ezlotnikova.service.model.ItemDTO;

public interface ItemService {

    ItemDTO add(ItemDTO item);

    List<ItemDTO> findAll();

    List<ItemDTO> findCompleted();

    int updateStatusById(Long id, String newStatus);

    int deleteItemsByStatus(String status);

    List<ItemDTO> getItems();

}