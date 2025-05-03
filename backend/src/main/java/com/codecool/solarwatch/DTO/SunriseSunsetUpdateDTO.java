package com.codecool.solarwatch.DTO;

import java.time.LocalDate;

public record SunriseSunsetUpdateDTO(String sunrise, String sunset, String cityName, LocalDate date) {
}
