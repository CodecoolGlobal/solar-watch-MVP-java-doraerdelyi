
package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.GeolocationReport;
import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.model.SunsetSunriseDTO;
import com.codecool.solarwatch.model.SunsetSunriseReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class SolarWatchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SolarWatchService solarWatchService;


    @Test
    void testGetSunsetAndSunriseByCity_success() {
        String city = "Budapest";
        GeolocationReport[] geolocationReports = {new GeolocationReport(47.4979937, 19.0403594)};
        SunsetSunriseDTO sunsetSunrise = new SunsetSunriseDTO("5:47:05", "16:08:45");
        SunsetSunriseReport sunsetSunriseReport = new SunsetSunriseReport(sunsetSunrise, "UTC");

        when(restTemplate.getForObject(anyString(), eq(GeolocationReport[].class))).thenReturn(geolocationReports);
        when(restTemplate.getForObject(anyString(), eq(SunsetSunriseReport.class))).thenReturn(sunsetSunriseReport);
        SunsetSunriseDTO result = solarWatchService.getSunsetAndSunriseByCity(city, null, "UTC");

        assertNotNull(result);
        assertEquals("5:47:05", result.sunrise());
        assertEquals("16:08:45", result.sunset());
    }

    @Test
    void testGetSunsetAndSunriseByCity_cityNotFound() {
        String cityToFind = "MyCity";
        when(restTemplate.getForObject(anyString(), eq(GeolocationReport[].class))).thenReturn(null);

        assertThrows(NoSuchCityException.class, () -> {
            solarWatchService.getSunsetAndSunriseByCity(cityToFind, null, "UTC");
        });
    }
}
