package com.gmail.ezlotnikova.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gmail.ezlotnikova.model.User;
import com.gmail.ezlotnikova.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public User loadUserByUsername(Connection connection, String username) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, username, password, role FROM user " +
                                "WHERE username = ?")
        ) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                } else {
                    return null;
                }
            }
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        Long id = rs.getLong("id");
        user.setId(id);
        String username = rs.getString("username");
        user.setUsername(username);
        String password = rs.getString("password");
        user.setPassword(password);
        String role = rs.getString("role");
        user.setRole(role);
        return user;
    }

}