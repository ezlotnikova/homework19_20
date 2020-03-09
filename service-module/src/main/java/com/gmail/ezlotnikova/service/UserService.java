package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.model.User;

public interface UserService {

    User loadUserByUsername(String username);

}