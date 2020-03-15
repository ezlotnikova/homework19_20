package com.gmail.ezlotnikova.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.ezlotnikova.model.Item;

public interface ItemRepository extends GenericRepository<Item> {

    Item add(Connection connection, Item item) throws SQLException;

    List<Item> findAll(Connection connection) throws SQLException;

    List<Item> findCompleted(Connection connection) throws SQLException;

    int updateStatusById(Connection connection, Long id, String newStatus) throws SQLException;

    int deleteItemsByStatus(Connection connection, String status) throws SQLException;

}