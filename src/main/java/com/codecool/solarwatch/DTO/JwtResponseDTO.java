package com.codecool.solarwatch.DTO;

import java.util.Set;

public record JwtResponseDTO(String token, String userName, Set<String> roles) {
}
