package com.gmail.ezlotnikova.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.model.Item;
import com.gmail.ezlotnikova.repository.ItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Item> implements ItemRepository {

    @Override
    public Item add(Connection connection, Item item) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO item(name, status) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getStatus());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding item failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Adding item failed, no ID obtained");
                }
            }
            return item;
        }
    }

    @Override
    public List<Item> findAll(Connection connection) throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            List<Item> items = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery(
                    "SELECT id, name, status FROM item")
            ) {
                while (resultSet.next()) {
                    Item item = getItem(resultSet);
                    items.add(item);
                }
                return items;
            }
        }
    }

    @Override
    public List<Item> findCompleted(Connection connection) throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet resultSet = statement.executeQuery(
                    "SELECT id, name, status FROM item WHERE status = 'COMPLETED'")) {
                List<Item> items = new ArrayList<>();
                while (resultSet.next()) {
                    Item item = getItem(resultSet);
                    items.add(item);
                }
                return items;
            }
        }
    }

    @Override
    public int updateStatusById(Connection connection, Long id, String newStatus) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE item SET status = ? WHERE id = ?")
        ) {
            statement.setString(1, newStatus);
            statement.setLong(2, id);
            return statement.executeUpdate();
        }
    }

    @Override
    public int deleteItemsByStatus(Connection connection, String status) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item WHERE status = ?")
        ) {
            statement.setString(1, status);
            return statement.executeUpdate();
        }
    }

    private Item getItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        Long id = resultSet.getLong("id");
        item.setId(id);
        String name = resultSet.getString("name");
        item.setName(name);
        String status = resultSet.getString("status");
        item.setStatus(status);
        return item;
    }

}