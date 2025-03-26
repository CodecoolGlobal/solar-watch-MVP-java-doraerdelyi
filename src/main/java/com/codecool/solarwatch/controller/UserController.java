package com.codecool.solarwatch.controller;


import com.codecool.solarwatch.DTO.JwtResponseDTO;
import com.codecool.solarwatch.DTO.UserCreateDTO;
import com.codecool.solarwatch.DTO.UserLoginDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @PostMapping("/register")
    public UUID createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return UUID.randomUUID();
    }

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody UserLoginDTO userLoginDTO) {
        return new JwtResponseDTO();
    }
}
