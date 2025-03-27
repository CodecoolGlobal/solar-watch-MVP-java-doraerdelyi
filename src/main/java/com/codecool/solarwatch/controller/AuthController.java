package com.codecool.solarwatch.controller;


import com.codecool.solarwatch.DTO.JwtResponseDTO;
import com.codecool.solarwatch.DTO.UserCreateDTO;
import com.codecool.solarwatch.DTO.UserLoginDTO;
import com.codecool.solarwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void createUser(@RequestBody UserCreateDTO userCreateDTO) {
        this.userService.registerUser(userCreateDTO);
    }

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody UserLoginDTO userLoginDTO) {
        return this.userService.loginUser(userLoginDTO);
    }
}
