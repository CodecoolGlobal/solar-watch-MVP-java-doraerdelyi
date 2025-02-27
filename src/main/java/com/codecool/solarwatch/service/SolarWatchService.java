package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.*;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetTimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SolarWatchService {
    private static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
    private static final String SUNRISE_SUNSET_API_URL = "https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s";

    private CityRepository cityRepository;
    private SunriseSunsetTimeRepository sunriseSunsetTimeRepository;

    @Value("${openweather.api.key}")
    private String OPENWEATHER_API_KEY;

    private final RestTemplate restTemplate;

    public SolarWatchService(RestTemplate restTemplate, CityRepository cityRepository, SunriseSunsetTimeRepository sunriseSunsetTimeRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
        this.sunriseSunsetTimeRepository = sunriseSunsetTimeRepository;
    }

    public SunsetSunrise getSunsetSunriseTimesByCityAndDate(String cityName, LocalDate date) {
        Optional<City> currentCity = this.cityRepository.findByName(cityName);
        if (currentCity.isPresent()) {
            Long cityId = currentCity.get().getId();
            Optional<SunriseSunsetTime> currentSunriseSunsetTime = this.sunriseSunsetTimeRepository.findByCityIdAndDate(cityId, date);
            if (currentSunriseSunsetTime.isPresent()) {
                LocalDateTime sunriseTime = currentSunriseSunsetTime.get().getSunriseTime();
                LocalDateTime sunsetTime = currentSunriseSunsetTime.get().getSunsetTime();
                return new SunsetSunrise(sunriseTime, sunsetTime);
            } else {
                double latitude = currentCity.get().getLatitude();
                double longitude = currentCity.get().getLongitude();
                SunriseSunsetTime sunriseSunsetTime = fetchSunriseSunsetTimeFromAPI(latitude, longitude, date);
                this.sunriseSunsetTimeRepository.save(sunriseSunsetTime);
                return new SunsetSunrise(sunriseSunsetTime.getSunriseTime(), sunriseSunsetTime.getSunsetTime());
                }
        } else {
            City city = fetchCityFromAPI(cityName);
            this.cityRepository.save(city);
            double latitude = city.getLatitude();
            double longitude = city.getLongitude();
            SunriseSunsetTime sunriseSunsetTime = fetchSunriseSunsetTimeFromAPI(latitude, longitude, date);
            this.sunriseSunsetTimeRepository.save(sunriseSunsetTime);
            return new SunsetSunrise(sunriseSunsetTime.getSunriseTime(), sunriseSunsetTime.getSunsetTime());
        }
    }

    private City fetchCityFromAPI(String cityName) {
        int limit = 1;
        String geocodingUrl = String.format(OPENWEATHER_API_URL, cityName, limit, OPENWEATHER_API_KEY);
        City[] cityResponse = restTemplate.getForObject(geocodingUrl, City[].class);
        if (cityResponse == null || cityResponse.length == 0) {
            throw new NoSuchCityException();
        }
        return cityResponse[0];
    }

    private SunriseSunsetTime fetchSunriseSunsetTimeFromAPI(double latitude, double longitude, LocalDate date) {
        String dateString = date == null ? LocalDate.now().toString() : date.toString();
        String sunsetSunriseUrl = String.format(SUNRISE_SUNSET_API_URL, latitude, longitude, dateString);
        SunriseSunsetTime sunriseSunsetTime = restTemplate.getForObject(sunsetSunriseUrl, SunriseSunsetTime.class);
        if (sunriseSunsetTime == null) {
            throw new NoSunriseSunsetDataException();
        }
        return sunriseSunsetTime;
    }
}
