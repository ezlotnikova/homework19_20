package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.model.User;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public AppUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        return new AppUser(user);
    }

}