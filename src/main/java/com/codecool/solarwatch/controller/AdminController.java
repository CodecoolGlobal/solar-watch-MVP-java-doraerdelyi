package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.DTO.*;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SolarWatchService solarWatchService;

    public AdminController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @PostMapping("/cities")
    public CityDTO createCity(@RequestBody CityCreateDTO cityCreateDTO) {
        return this.solarWatchService.createCity(cityCreateDTO);
    }

    @PutMapping("/cities/{id}")
    public CityDTO updateCity(@PathVariable UUID id, @RequestBody CityUpdateDTO cityUpdateDTO) {
        return this.solarWatchService.updateCity(id, cityUpdateDTO);
    }

    @DeleteMapping("/cities/{id}")
    public void deleteCity(@PathVariable UUID id) {
        this.solarWatchService.deleteCity(id);
    }

    @PostMapping("/sunrise-sunset")
    public SunriseSunsetDTO createSunriseSunsetTimes(@RequestBody SunriseSunsetCreateDTO sunriseSunsetCreateDTO) {
        return this.solarWatchService.createSunriseSunsetTimes(sunriseSunsetCreateDTO);
    }

    @PutMapping("/cities/{id}")
    public SunriseSunsetDTO updateSunriseSunsetTimes(@PathVariable UUID id, @RequestBody SunriseSunsetUpdateDTO sunriseSunsetUpdateDTO) {
        return this.solarWatchService.updateSunriseSunsetTimes(id, sunriseSunsetUpdateDTO);
    }

    @DeleteMapping("/cities/{id}")
    public void deleteSunriseSunsetTimes(@PathVariable UUID id) {
        this.solarWatchService.deleteSunriseSunsetTimes(id);
    }
}
