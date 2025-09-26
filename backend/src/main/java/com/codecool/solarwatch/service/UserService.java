package com.codecool.solarwatch.service;

import com.codecool.solarwatch.DTO.JwtResponseDTO;
import com.codecool.solarwatch.DTO.UserCreateDTO;
import com.codecool.solarwatch.DTO.UserLoginDTO;
import com.codecool.solarwatch.model.user.Role;
import com.codecool.solarwatch.model.user.RoleType;
import com.codecool.solarwatch.model.user.SunriseSunsetUser;
import com.codecool.solarwatch.exception.UserAlreadyExistsException;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.repository.UserRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    /**
     * Registers a new user with the default user role.
     *
     * @param userCreateDTO UserCreateDTO data transfer object containing the email address and the password.
     * @throws UserAlreadyExistsException if a user with the given email address already exists.
     */
    public void registerUser(UserCreateDTO userCreateDTO) {
        Optional<SunriseSunsetUser> existingUser = this.userRepository.findByEmail(userCreateDTO.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        Role role = this.roleRepository.findByRoleType(RoleType.ROLE_USER).get();
        String encodedPassword = encoder.encode(userCreateDTO.password());
        SunriseSunsetUser user = new SunriseSunsetUser(userCreateDTO.email(), encodedPassword, Set.of(role));
        //this.userRepository.save(user);
    }

    /**
     * Authenticates the user and generates a JWT token.
     *
     * @param userLoginDTO UserLoginDTO data transfer object containing login credentials.
     * @return JwtResponseDTO data transfer object containing JWT token, username, and roles.
     */
    public JwtResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.email(), userLoginDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new JwtResponseDTO(jwtToken, userDetails.getUsername(), roles);
    }
}
