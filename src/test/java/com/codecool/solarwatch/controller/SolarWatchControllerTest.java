
package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunsetSunriseDTO;
import com.codecool.solarwatch.service.SolarWatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.codecool.solarwatch.model.NoSuchCityException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class SolarWatchControllerTest {

    @Mock
    private SolarWatchService solarWatchService;

    @InjectMocks
    private SolarWatchController solarWatchController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(solarWatchController).setControllerAdvice(new SolarWatchControllerAdvice()).build();
    }

    @Test
    void testGetSunriseAndSunset_success() throws Exception {
        String city = "Budapest";
        SunsetSunriseDTO sunriseSunset = new SunsetSunriseDTO("5:47:05", "16:08:45");
        when(solarWatchService.getSunsetSunriseTimesByCityAndDate(city, null)).thenReturn(sunriseSunset);
        mockMvc.perform(get("/api/solarwatch/sunrise-sunset?city=" + city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sunrise").value("5:47:05"))
                .andExpect(jsonPath("$.sunset").value("16:08:45"));
    }

    @Test
    void testGetSunriseAndSunset_cityNotFound() throws Exception {
        String city = "MyCity";
        when(solarWatchService.getSunsetSunriseTimesByCityAndDate(city, null))
                .thenThrow(new NoSuchCityException());

        mockMvc.perform(get("/api/solarwatch/sunrise-sunset?city=" + city))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("No such city found"));
    }
}