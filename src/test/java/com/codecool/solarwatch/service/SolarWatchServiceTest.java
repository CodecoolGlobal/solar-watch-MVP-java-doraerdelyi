package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.*;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private SunriseSunsetTimeRepository sunriseSunsetTimeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SolarWatchService solarWatchService;

    private City testCity;
    private LocalDate testDate;
    private SunriseSunsetTime testSunriseSunsetTime;
    private SunsetSunriseResponseDTO testApiResponse;

    @BeforeEach
    void setUp() {
        testCity = new City("Budapest", 47.4979, 19.0402, "Hungary", "Budapest");
        testCity.setId(1L);

        testDate = LocalDate.of(2024, 2, 27);

        testSunriseSunsetTime = new SunriseSunsetTime("06:30:00", "18:45:00", testDate, testCity);

        testApiResponse = new SunsetSunriseResponseDTO(
                new SunsetSunriseDTO("06:30:00", "18:45:00")
        );
    }

    @Test
    void getSunsetSunriseTimesByCityAndDate_ShouldReturnExistingSunriseSunsetTime() {
        when(cityRepository.findByName("Budapest")).thenReturn(Optional.of(testCity));
        when(sunriseSunsetTimeRepository.findByCityIdAndDate(testCity.getId(), testDate))
                .thenReturn(Optional.of(testSunriseSunsetTime));

        SunsetSunriseDTO result = solarWatchService.getSunsetSunriseTimesByCityAndDate("Budapest", testDate);

        assertEquals("06:30:00", result.sunrise());
        assertEquals("18:45:00", result.sunset());

        verify(cityRepository, times(1)).findByName("Budapest");
        verify(sunriseSunsetTimeRepository, times(1)).findByCityIdAndDate(testCity.getId(), testDate);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getSunsetSunriseTimesByCityAndDate_ShouldFetchFromAPI_WhenDataIsNotInDatabase() {
        when(cityRepository.findByName("Budapest")).thenReturn(Optional.of(testCity));
        when(sunriseSunsetTimeRepository.findByCityIdAndDate(testCity.getId(), testDate)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(SunsetSunriseResponseDTO.class))).thenReturn(testApiResponse);

        SunsetSunriseDTO result = solarWatchService.getSunsetSunriseTimesByCityAndDate("Budapest", testDate);

        assertEquals("06:30:00", result.sunrise());
        assertEquals("18:45:00", result.sunset());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(SunsetSunriseResponseDTO.class));
        verify(sunriseSunsetTimeRepository, times(1)).save(any(SunriseSunsetTime.class));
    }

    @Test
    void getSunsetSunriseTimesByCityAndDate_ShouldFetchCityFromAPI_WhenCityIsNotInDatabase() {
        when(cityRepository.findByName("Budapest")).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(City[].class))).thenReturn(new City[]{testCity});
        when(restTemplate.getForObject(anyString(), eq(SunsetSunriseResponseDTO.class))).thenReturn(testApiResponse);

        SunsetSunriseDTO result = solarWatchService.getSunsetSunriseTimesByCityAndDate("Budapest", testDate);

        assertEquals("06:30:00", result.sunrise());
        assertEquals("18:45:00", result.sunset());

        verify(cityRepository, times(1)).save(testCity);
        verify(sunriseSunsetTimeRepository, times(1)).save(any(SunriseSunsetTime.class));
    }

    @Test
    void getSunsetSunriseTimesByCityAndDate_ShouldThrowException_WhenCityNotFoundInAPI() {
        when(cityRepository.findByName("UnknownCity")).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(City[].class))).thenReturn(new City[]{});

        assertThrows(NoSuchCityException.class, () ->
                solarWatchService.getSunsetSunriseTimesByCityAndDate("UnknownCity", testDate)
        );

        verify(cityRepository, never()).save(any(City.class));
        verify(sunriseSunsetTimeRepository, never()).save(any(SunriseSunsetTime.class));
    }

    @Test
    void getSunsetSunriseTimesByCityAndDate_ShouldThrowException_WhenAPIResponseIsNull() {
        when(cityRepository.findByName("Budapest")).thenReturn(Optional.of(testCity));
        when(sunriseSunsetTimeRepository.findByCityIdAndDate(testCity.getId(), testDate)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(SunsetSunriseResponseDTO.class))).thenReturn(null);

        assertThrows(NoSunriseSunsetDataException.class, () ->
                solarWatchService.getSunsetSunriseTimesByCityAndDate("Budapest", testDate)
        );

        verify(sunriseSunsetTimeRepository, never()).save(any(SunriseSunsetTime.class));
    }
}

