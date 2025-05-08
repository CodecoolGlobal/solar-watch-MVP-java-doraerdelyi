package com.codecool.solarwatch.config;

import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.model.RoleType;
import com.codecool.solarwatch.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Set<RoleType> predefinedRoles = Set.of(RoleType.ROLE_USER, RoleType.ROLE_ADMIN);

        for (RoleType roleType : predefinedRoles) {
            if (roleRepository.findByRoleType(roleType).isEmpty()) {
                roleRepository.save(new Role(roleType));
            }
        }
    }
}
