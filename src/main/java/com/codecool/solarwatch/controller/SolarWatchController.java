package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.DTO.SunsetSunriseDTO;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/solarwatch")
public class SolarWatchController {

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/sunrise-sunset")
    public SunsetSunriseDTO getSunriseAndSunset(@RequestParam String city, @RequestParam(required = false) LocalDate
date) {
        return solarWatchService.getSunsetSunriseTimesByCityAndDate(city, date);
    }


}