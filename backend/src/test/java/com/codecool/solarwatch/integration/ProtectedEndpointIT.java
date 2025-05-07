package com.codecool.solarwatch.integration;

import com.codecool.solarwatch.DTO.JwtResponseDTO;
import com.codecool.solarwatch.DTO.UserLoginDTO;
import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.model.RoleType;
import com.codecool.solarwatch.model.SunriseSunsetUser;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProtectedEndpointIT {

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

    private String jwtToken;


    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        SunriseSunsetUser user = new SunriseSunsetUser("testuser@example.com", passwordEncoder.encode("password123"), Set.of(roleUser));
        userRepository.save(user);
        UserLoginDTO loginDTO = new UserLoginDTO("testuser@example.com", "password123");

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JwtResponseDTO jwtResponse = objectMapper.readValue(response, JwtResponseDTO.class);
        jwtToken = jwtResponse.token();
    }


    @Test
    public void testProtectedEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/api/solarwatch/sunrise-sunset"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testProtectedEndpointAuthorized() throws Exception {
        mockMvc.perform(get("/api/solarwatch/sunrise-sunset")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}

