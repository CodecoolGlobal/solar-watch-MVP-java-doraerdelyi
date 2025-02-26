package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.SunriseSunsetTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SunriseSunsetTimeRepository extends JpaRepository<SunriseSunsetTime, Long> {
}
