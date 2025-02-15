package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunsetSunrise;
import com.codecool.solarwatch.model.SunsetSunriseReport;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/solarwatch")
public class SolarWatchController {

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/sunrise-sunset")
    public SunsetSunrise getSunriseAndSunset(@RequestParam String city, @RequestParam(required = false) LocalDate
date, @RequestParam(defaultValue = "UTC") String timezone) {
        return solarWatchService.getSunsetAndSunriseByCity(city, date, timezone);
    }

}