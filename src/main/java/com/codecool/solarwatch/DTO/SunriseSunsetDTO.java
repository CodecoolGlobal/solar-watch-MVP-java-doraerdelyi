package com.codecool.solarwatch.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetDTO(String sunrise, String sunset) {
}
