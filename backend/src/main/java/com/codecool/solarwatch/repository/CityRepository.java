package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
    Optional<City> findByPublicId(UUID publicId);
    void deleteByPublicId(UUID publicId);
}
