package com.codecool.solarwatch.service;

import com.codecool.solarwatch.DTO.JwtResponseDTO;
import com.codecool.solarwatch.DTO.UserCreateDTO;
import com.codecool.solarwatch.DTO.UserLoginDTO;
import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.model.RoleType;
import com.codecool.solarwatch.model.SunriseSunsetUser;
import com.codecool.solarwatch.model.UserAlreadyExistsException;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.repository.UserRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private User userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_createsUserSuccessfully() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("test@example.com", "password");
        Role role = new Role(RoleType.ROLE_USER);
        SunriseSunsetUser savedUser = new SunriseSunsetUser("test@example.com", "password", Set.of(role));
        when(roleRepository.findByRoleType(RoleType.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(SunriseSunsetUser.class))).thenReturn(savedUser);
        userService.registerUser(userCreateDTO);
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(SunriseSunsetUser.class));
    }

    @Test
    void registerUser_throwsIfEmailAlreadyExists() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("existingUser@test.com", "password");
        when(userRepository.findByEmail(userCreateDTO.email())).thenReturn(Optional.of(new SunriseSunsetUser()));
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(userCreateDTO);
        });
        verify(userRepository).findByEmail(userCreateDTO.email());
        verify(userRepository, never()).save(any(SunriseSunsetUser.class));
    }

    @Test
    void authenticateUser_shouldReturnJwtResponse() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("existingUser@test.com", "password");
        String jwtToken = "jwtToken";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwtToken);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("existingUser@test.com");
        when(userDetails.getAuthorities()).thenReturn(Set.of(() -> "ROLE_USER"));
        JwtResponseDTO response = userService.loginUser(userLoginDTO);
        assertEquals(jwtToken, response.token());
        assertEquals(userLoginDTO.email(), response.userName());
        assertTrue(response.roles().contains("ROLE_USER"));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();
        verify(userDetails).getAuthorities();
    }
}
