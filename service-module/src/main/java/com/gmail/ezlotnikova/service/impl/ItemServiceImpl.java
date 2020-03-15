package com.gmail.ezlotnikova.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.ezlotnikova.model.Item;
import com.gmail.ezlotnikova.repository.ItemRepository;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.service.model.constant.ItemStatusEnum;
import com.gmail.ezlotnikova.service.model.constant.UserRoleEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = itemRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item databaseItem = convertDTOToDatabaseItem(itemDTO);
                Item addedItem = itemRepository.add(connection, databaseItem);
                ItemDTO addedItemDTO = convertDatabaseItemToDTO(addedItem);
                connection.commit();
                return addedItemDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return itemDTO;
    }

    @Override
    public List<ItemDTO> getItems() {
        List<ItemDTO> items = new ArrayList<>();
        if (hasRole(UserRoleEnum.ROLE_USER)) {
            items = findCompleted();
        }
        if (hasRole(UserRoleEnum.ROLE_ADMIN)) {
            items = findAll();
        }
        return items;
    }

    @Override
    public List<ItemDTO> findAll() {
        try (Connection connection = itemRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Item> items = itemRepository.findAll(connection);
                List<ItemDTO> itemDTOList = new ArrayList<>();
                for (Item item : items) {
                    ItemDTO itemDTO = convertDatabaseItemToDTO(item);
                    itemDTOList.add(itemDTO);
                }
                connection.commit();
                return itemDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ItemDTO> findCompleted() {
        try (Connection connection = itemRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Item> items = itemRepository.findCompleted(connection);
                List<ItemDTO> itemDTOList = new ArrayList<>();
                for (Item item : items) {
                    ItemDTO itemDTO = convertDatabaseItemToDTO(item);
                    itemDTOList.add(itemDTO);
                }
                connection.commit();
                return itemDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public int updateStatusById(Long id, String newStatus) {
        int rowsUpdated = 0;
        try (Connection connection = itemRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                rowsUpdated = itemRepository.updateStatusById(connection, id, newStatus);
                connection.commit();
                return rowsUpdated;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return rowsUpdated;
    }

    @Override
    public int deleteItemsByStatus(String status) {
        int rowsDeleted = 0;
        try (Connection connection = itemRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                rowsDeleted = itemRepository.deleteItemsByStatus(connection, status);
                connection.commit();
                return rowsDeleted;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return rowsDeleted;
    }

    private boolean hasRole(UserRoleEnum role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }

    private Item convertDTOToDatabaseItem(ItemDTO itemDTO) {
        Item item = new Item();
        String name = itemDTO.getName();
        item.setName(name);
        String status = itemDTO.getStatus().name();
        item.setStatus(status);
        return item;
    }

    private ItemDTO convertDatabaseItemToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        Long id = item.getId();
        itemDTO.setId(id);
        String name = item.getName();
        itemDTO.setName(name);
        String status = item.getStatus();
        itemDTO.setStatus(ItemStatusEnum.valueOf(status));
        return itemDTO;
    }

}