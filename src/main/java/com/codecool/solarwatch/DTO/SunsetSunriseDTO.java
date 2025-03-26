package com.codecool.solarwatch.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunsetSunriseDTO(String sunrise, String sunset) {
}
