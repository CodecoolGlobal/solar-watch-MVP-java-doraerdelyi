package com.codecool.solarwatch.service;

import com.codecool.solarwatch.DTO.*;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.model.NoSunriseSunsetDataException;
import com.codecool.solarwatch.model.SunriseSunsetTime;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SolarWatchServiceTest.class);
    @Mock
    private WebClient webClient;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec uriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private CityRepository cityRepository;
    @Mock
    private SunriseSunsetTimeRepository timeRepository;

    @InjectMocks
    private SolarWatchService service;

    private LocalDate date;
    private City city;
    private SunriseSunsetResponseDTO responseDTO;
    private SunriseSunsetTime time;
    private UUID publicId;

    @BeforeEach
    void setup() {
        date = LocalDate.now();
        publicId = UUID.randomUUID();
        city = new City("TestCity", 10.0, 20.0, "TestCountry", "TestState");
        city.setId(1L);
        city.setPublicId(publicId);
        responseDTO = new SunriseSunsetResponseDTO(new SunriseSunsetDTO("06:00:00", "18:00:00"));
        time = new SunriseSunsetTime("06:00:00", "18:00:00", date, city);
    }

    @Test
    void getSunsetSunriseTimes_returnsExistingTimeFromDb() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.findByCityIdAndDate(1L, date)).thenReturn(Optional.of(time));
        SunriseSunsetDTO result = service.getSunsetSunriseTimesByCityAndDate("TestCity", date);
        assertEquals("06:00:00", result.sunrise());
        assertEquals("18:00:00", result.sunset());
        verify(timeRepository).findByCityIdAndDate(1L, date);
        verifyNoInteractions(webClient);
    }

    @Test
    void getSunsetSunriseTimes_fetchesFromApiIfNotInDb() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.findByCityIdAndDate(1L, date)).thenReturn(Optional.empty());
        when(timeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        mockWebClientSunriseCall(responseDTO);
        SunriseSunsetDTO result = service.getSunsetSunriseTimesByCityAndDate("TestCity", date);
        assertEquals("06:00:00", result.sunrise());
        verify(timeRepository).save(any());
    }

    @Test
    void getSunsetSunriseTimes_fetchesCityFromApiIfNotFound() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.empty());
        when(cityRepository.save(any())).thenReturn(city);
        when(timeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        mockWebClientCityCall(new City[]{city});
        mockWebClientSunriseCall(responseDTO);
        SunriseSunsetDTO result = service.getSunsetSunriseTimesByCityAndDate("TestCity", date);
        assertEquals("06:00:00", result.sunrise());
        assertEquals("18:00:00", result.sunset());
        verify(cityRepository).save(city);
    }

    @Test
    void getSunsetSunriseTimes_throwsIfCityApiReturnsEmpty() {
        when(cityRepository.findByName("Nowhere")).thenReturn(Optional.empty());
        mockWebClientCityCall(new City[]{});
        assertThrows(NoSuchCityException.class, () -> service.getSunsetSunriseTimesByCityAndDate("Nowhere", date));
    }

    @Test
    void getSunsetSunriseTimes_throwsIfSunriseApiReturnsNull() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.findByCityIdAndDate(1L, date)).thenReturn(Optional.empty());
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetResponseDTO.class)).thenReturn(Mono.empty());
        assertThrows(NoSunriseSunsetDataException.class, () ->
                service.getSunsetSunriseTimesByCityAndDate("TestCity", date)
        );
    }

    @Test
    void createCity_createsCitySuccessfully() {
        CityCreateDTO dto = new CityCreateDTO("City", 1.0, 2.0, "Country", null);
        CityDTO result = service.createCity(dto);
        assertEquals("Unknown", result.state());
        assertEquals("City", result.name());
        verify(cityRepository).save(any());
    }

    @Test
    void updateCity_updatesCitySuccessfully() {
        when(cityRepository.findByPublicId(publicId)).thenReturn(Optional.of(city));
        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CityUpdateDTO dto = new CityUpdateDTO("NewName", 1.0, 2.0, "NewCountry", null);
        CityDTO result = service.updateCity(publicId, dto);
        assertEquals("Unknown", result.state());
        assertEquals("NewName", result.name());
        assertEquals("NewCountry", result.country());
        verify(cityRepository).save(any());
    }

    @Test
    void updateCity_throwsIfCityToUpdateNotFound() {
        when(cityRepository.findByPublicId(publicId)).thenReturn(Optional.empty());
        CityUpdateDTO dto = new CityUpdateDTO("NewName", 1.0, 2.0, "NewCountry", "State");
        assertThrows(NoSuchCityException.class, () -> service.updateCity(publicId, dto));
        verify(cityRepository).findByPublicId(publicId);
    }

    @Test
    void deleteCity_deletesCityByPublicId() {
        service.deleteCity(publicId);
        verify(cityRepository).deleteByPublicId(publicId);
    }

    @Test
    void createSunriseSunsetTimes_createsSunriseSunsetTimeSuccessfully() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.save(any(SunriseSunsetTime.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SunriseSunsetCreateDTO dto = new SunriseSunsetCreateDTO("06:00:00", "18:00:00", "TestCity", date);
        SunriseSunsetDTO result = service.createSunriseSunsetTimes(dto);
        assertEquals("06:00:00", result.sunrise());
        assertEquals("18:00:00", result.sunset());
        verify(timeRepository).save(any());
    }

    @Test
    void createSunriseSunsetTimes_createsSunriseSunsetTimeWithFetchedCity() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.empty());
        when(timeRepository.save(any(SunriseSunsetTime.class))).thenAnswer(invocation -> invocation.getArgument(0));
        mockWebClientCityCall(new City[]{city});
        SunriseSunsetCreateDTO dto = new SunriseSunsetCreateDTO("06:00:00", "18:00:00", "TestCity", date);
        SunriseSunsetDTO result = service.createSunriseSunsetTimes(dto);
        assertEquals("06:00:00", result.sunrise());
        assertEquals("18:00:00", result.sunset());
        verify(timeRepository).save(any());
    }

    @Test
    void updateSunriseSunsetTimes_updatesSunriseSunsetTimeSuccessfully() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.findByCityIdAndDate(city.getId(), date)).thenReturn(Optional.of(time));
        when(timeRepository.save(any(SunriseSunsetTime.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SunriseSunsetUpdateDTO dto = new SunriseSunsetUpdateDTO("07:00:00", "19:00:00", "TestCity", date);
        SunriseSunsetDTO result = service.updateSunriseSunsetTimes(dto);
        assertEquals("07:00:00", result.sunrise());
        verify(timeRepository).save(any());
    }

    @Test
    void updateSunriseSunsetTimes_throwsWhenMissingSunriseData() {
        when(cityRepository.findByName("TestCity")).thenReturn(Optional.of(city));
        when(timeRepository.findByCityIdAndDate(city.getId(), date)).thenReturn(Optional.empty());
        SunriseSunsetUpdateDTO dto = new SunriseSunsetUpdateDTO("07:00:00", "19:00:00", "TestCity", date);
        assertThrows(NoSunriseSunsetDataException.class, () -> service.updateSunriseSunsetTimes(dto));
    }

    @Test
    void deleteSunriseSunsetTimes_deletesSunriseSunsetByPublicId() {
        service.deleteSunriseSunsetTimes(publicId);
        verify(timeRepository).deleteByPublicId(publicId);
    }


    private void mockWebClientCityCall(City[] cities) {
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(City[].class)).thenReturn(Mono.just(cities));
    }

    private void mockWebClientSunriseCall(SunriseSunsetResponseDTO response) {
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SunriseSunsetResponseDTO.class)).thenReturn(Mono.just(response));
    }
}