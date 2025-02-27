package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.SunriseSunsetTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SunriseSunsetTimeRepository extends JpaRepository<SunriseSunsetTime, Long> {
    Optional<SunriseSunsetTime> findByCityIdAndDate(Long cityId, LocalDate date);
}
