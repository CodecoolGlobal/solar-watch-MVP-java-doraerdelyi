package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.*;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetTimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    public SunsetSunrise getSunsetAndSunriseByCity(String city, LocalDate date, String timezone) {
        int limit = 1;
        String geocodingUrl = String.format(OPENWEATHER_API_URL, city, limit, OPENWEATHER_API_KEY);
        GeolocationReport[] geocodingResponse = restTemplate.getForObject(geocodingUrl, GeolocationReport[].class);
        if (geocodingResponse == null || geocodingResponse.length == 0) {
            System.out.println("yay");
            throw new NoSuchCityException();
        }
        double latitude = geocodingResponse[0].lat();
        double longitude = geocodingResponse[0].lon();
        String dateString = date == null ? LocalDate.now().toString() : date.toString();
        String sunsetSunriseUrl = String.format(SUNRISE_SUNSET_API_URL, latitude, longitude, dateString);
        SunsetSunriseReport report = restTemplate.getForObject(sunsetSunriseUrl, SunsetSunriseReport.class);
        if (report == null || report.results() == null) {
            throw new NoSunriseSunsetDataException();
        }
        return report.results();
    }

    public SunsetSunrise getSunsetSunriseTimesByCityAndDate(String cityName, LocalDate date) {
        Optional<City> currentCity = this.cityRepository.findByName(cityName);
        if (currentCity.isPresent()) {

        } else {

        }
    }
}
