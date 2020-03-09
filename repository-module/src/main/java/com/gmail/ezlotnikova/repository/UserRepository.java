package com.gmail.ezlotnikova.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.ezlotnikova.model.User;

public interface UserRepository extends GenericRepository<User> {

    User loadUserByUsername(Connection connection, String username) throws SQLException;

}