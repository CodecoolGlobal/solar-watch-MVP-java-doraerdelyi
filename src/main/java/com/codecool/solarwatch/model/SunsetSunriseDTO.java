package com.codecool.solarwatch.model;

import java.time.LocalDateTime;

public record SunsetSunriseDTO(LocalDateTime sunrise, LocalDateTime sunset) {
}
