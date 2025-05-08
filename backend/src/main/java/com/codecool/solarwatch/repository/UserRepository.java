package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.user.SunriseSunsetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SunriseSunsetUser, Long> {
    Optional<SunriseSunsetUser> findByEmail(String email);
}
