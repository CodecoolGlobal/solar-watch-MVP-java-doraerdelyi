package com.codecool.solarwatch.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseSunsetResponseDTO(SunriseSunsetDTO results) {
}
