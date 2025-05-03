package com.codecool.solarwatch.security.service;

import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.model.SunriseSunsetUser;
import com.codecool.solarwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        SunriseSunsetUser sunriseSunsetUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Role role : sunriseSunsetUser.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getRoleType().toString()));
        }
        return new User(sunriseSunsetUser.getEmail(), sunriseSunsetUser.getPassword(), roles);
    }
}
