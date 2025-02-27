package com.codecool.solarwatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunsetSunriseResponseDTO(SunsetSunriseDTO results) {
}
