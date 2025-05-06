
package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.DTO.SunriseSunsetDTO;
import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.service.SolarWatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        SunriseSunsetDTO sunriseSunset = new SunriseSunsetDTO("5:47:05", "16:08:45");
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