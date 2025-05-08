package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.user.Role;
import com.codecool.solarwatch.model.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
