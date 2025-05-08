package com.codecool.solarwatch.integration;

import com.codecool.solarwatch.DTO.UserLoginDTO;
import com.codecool.solarwatch.model.user.Role;
import com.codecool.solarwatch.model.user.RoleType;
import com.codecool.solarwatch.model.user.SunriseSunsetUser;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserLoginIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        SunriseSunsetUser user = new SunriseSunsetUser("testuser@example.com", passwordEncoder.encode("password123"), Set.of(roleUser));
        userRepository.save(user);
    }


    @Test
    public void testUserLogin() throws Exception {
        UserLoginDTO loginUser = new UserLoginDTO("testuser@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}

