package com.codecool.solarwatch.DTO;

import java.time.LocalDate;

public record SunriseSunsetCreateDTO(String sunrise, String sunset, String cityName, LocalDate date) {
}
