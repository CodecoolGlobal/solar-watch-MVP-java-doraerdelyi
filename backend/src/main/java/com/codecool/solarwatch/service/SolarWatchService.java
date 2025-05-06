package com.codecool.solarwatch.service;

import com.codecool.solarwatch.DTO.*;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.NoSuchCityException;
import com.codecool.solarwatch.model.NoSunriseSunsetDataException;
import com.codecool.solarwatch.model.SunriseSunsetTime;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseSunsetTimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class SolarWatchService {
    private static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String SUNRISE_SUNSET_API_URL = "https://api.sunrise-sunset.org/json";
    private static final int CITY_API_RESULT_LIMIT = 1;
    private static final String DEFAULT_STATE = "Unknown";

    private final CityRepository cityRepository;
    private final SunriseSunsetTimeRepository sunriseSunsetTimeRepository;

    @Value("${openweather.api.key}")
    private String OPENWEATHER_API_KEY;

    private final WebClient webClient;

    public SolarWatchService(WebClient webClient, CityRepository cityRepository, SunriseSunsetTimeRepository sunriseSunsetTimeRepository) {
        this.webClient = webClient;
        this.cityRepository = cityRepository;
        this.sunriseSunsetTimeRepository = sunriseSunsetTimeRepository;
    }


    /**
     * Retrieves sunrise and sunset times for a given city and date.
     * If the city or the sunrise/sunset data is not found in the database, it will fetch from external APIs and store them.
     *
     * @param cityName name of the city
     * @param date     optional date for the sunrise/sunset data (uses current date if null)
     * @return SunriseSunsetDTO sunrise and sunset time data transfer object
     */
    public SunriseSunsetDTO getSunsetSunriseTimesByCityAndDate(String cityName, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        City city = cityRepository.findByName(cityName)
                .orElseGet(() -> saveCityFromAPI(cityName));
        return sunriseSunsetTimeRepository.findByCityIdAndDate(city.getId(), targetDate)
                .map(data -> new SunriseSunsetDTO(data.getSunriseTime(), data.getSunsetTime()))
                .orElseGet(() -> {
                    SunriseSunsetTime sunriseSunsetTime = saveSunriseSunsetTimeFromAPI(targetDate, city);
                    return new SunriseSunsetDTO(sunriseSunsetTime.getSunriseTime(), sunriseSunsetTime.getSunsetTime());
                });
    }


    /**
     * Creates and saves a new city in the database from provided details.
     *
     * @param cityCreateDTO data transfer object containing the city data to create
     * @return the created city's data as a data transfer object
     */
    public CityDTO createCity(CityCreateDTO cityCreateDTO) {
        City city = new City(
                cityCreateDTO.name(),
                cityCreateDTO.latitude(),
                cityCreateDTO.longitude(),
                cityCreateDTO.country(),
                cityCreateDTO.state() == null ? DEFAULT_STATE : cityCreateDTO.state()
        );
        this.cityRepository.save(city);
        return convertCityToCityDTO(city);
    }


    /**
     * Updates an existing city's details identified by its public ID.
     *
     * @param publicId      unique public identifier of the city
     * @param cityUpdateDTO data transfer object containing updated city details
     * @return the updated city's data as a DTO
     * @throws NoSuchCityException if the city does not exist
     */
    public CityDTO updateCity(UUID publicId, CityUpdateDTO cityUpdateDTO) {
        City city = this.cityRepository.findByPublicId(publicId).map(currentCity -> {
                    currentCity.setName(cityUpdateDTO.name());
                    currentCity.setLatitude(cityUpdateDTO.latitude());
                    currentCity.setLongitude(cityUpdateDTO.longitude());
                    currentCity.setCountry(cityUpdateDTO.country());
                    currentCity.setState(cityUpdateDTO.state() == null ? DEFAULT_STATE : cityUpdateDTO.state());
                    return this.cityRepository.save(currentCity);
                })
                .orElseThrow(NoSuchCityException::new);
        return convertCityToCityDTO(city);
    }


    /**
     * Deletes a city from the database by its public ID.
     *
     * @param publicId unique public identifier of the city to delete
     */
    public void deleteCity(UUID publicId) {
        this.cityRepository.deleteByPublicId(publicId);
    }


    /**
     * Creates and saves new sunrise and sunset time data for a given city and date.
     * If the city does not exist, it is fetched from the API and stored first.
     *
     * @param sunriseSunsetCreateDTO data transfer object containing the sunrise/sunset data to create
     * @return created sunrise and sunset time data as a data transfer object
     */
    public SunriseSunsetDTO createSunriseSunsetTimes(SunriseSunsetCreateDTO sunriseSunsetCreateDTO) {
        City city = this.cityRepository.findByName(sunriseSunsetCreateDTO.cityName())
                .orElseGet(() -> saveCityFromAPI(sunriseSunsetCreateDTO.cityName()));
        SunriseSunsetTime sunriseSunsetTime = this.sunriseSunsetTimeRepository
                .save(new SunriseSunsetTime(sunriseSunsetCreateDTO.sunrise(),
                        sunriseSunsetCreateDTO.sunset(),
                        sunriseSunsetCreateDTO.date(),
                        city));
        return new SunriseSunsetDTO(sunriseSunsetTime.getSunriseTime(),
                sunriseSunsetTime.getSunsetTime());
    }


    /**
     * Updates existing sunrise and sunset data for a city on a given date.
     *
     * @param sunriseSunsetUpdateDTO DTO containing updated data
     * @return updated sunrise and sunset time data as a DTO
     * @throws NoSuchCityException          if the city does not exist
     * @throws NoSunriseSunsetDataException if the sunrise/sunset data cannot be found
     */
    public SunriseSunsetDTO updateSunriseSunsetTimes(SunriseSunsetUpdateDTO sunriseSunsetUpdateDTO) {
        City city = this.cityRepository.findByName(sunriseSunsetUpdateDTO.cityName())
                .orElseThrow(NoSuchCityException::new);
        SunriseSunsetTime sunriseSunsetTime = this.sunriseSunsetTimeRepository.findByCityIdAndDate(city.getId(), sunriseSunsetUpdateDTO.date())
                .map(sunriseSunset -> {
                    sunriseSunset.setSunriseTime(sunriseSunsetUpdateDTO.sunrise());
                    sunriseSunset.setSunsetTime(sunriseSunsetUpdateDTO.sunset());
                    sunriseSunset.setDate(sunriseSunsetUpdateDTO.date());
                    return this.sunriseSunsetTimeRepository.save(sunriseSunset);
                })
                .orElseThrow(NoSunriseSunsetDataException::new);
        return new SunriseSunsetDTO(sunriseSunsetTime.getSunriseTime(), sunriseSunsetTime.getSunsetTime());
    }


    /**
     * Deletes a sunrise and sunset time entry from the database using its public ID.
     *
     * @param publicId unique public identifier of the sunrise/sunset record to delete
     */
    public void deleteSunriseSunsetTimes(UUID publicId) {
        this.sunriseSunsetTimeRepository.deleteByPublicId(publicId);
    }


    /**
     * Fetches city data from the OpenWeather API and saves the first result to the database.
     *
     * @param cityName the name of the city to fetch
     * @return the saved City entity
     * @throws NoSuchCityException if the API returns no results
     */
    private City saveCityFromAPI(String cityName) {
        String geocodingUrl = UriComponentsBuilder
                .fromUriString(OPENWEATHER_API_URL)
                .queryParam("q", cityName)
                .queryParam("limit", CITY_API_RESULT_LIMIT)
                .queryParam("appid", OPENWEATHER_API_KEY)
                .build()
                .toUriString();
        City[] cityResponse = this.webClient.get()
                .uri(geocodingUrl)
                .retrieve()
                .bodyToMono(City[].class)
                .block();
        if (cityResponse == null || cityResponse.length == 0) {
            throw new NoSuchCityException();
        }
        return this.cityRepository.save(cityResponse[0]);
    }

    /**
     * Fetches sunrise and sunset data from the external API, converts it to an entity, and saves it to the database.
     *
     * @param date the date for which sunrise and sunset times are requested
     * @param city the city for which the data is relevant
     * @return the saved SunriseSunsetTime entity
     * @throws NoSunriseSunsetDataException if the API returns null or invalid data
     */
    private SunriseSunsetTime saveSunriseSunsetTimeFromAPI(LocalDate date, City city) {
        String sunsetSunriseUrl = UriComponentsBuilder
                .fromUriString(SUNRISE_SUNSET_API_URL)
                .queryParam("lat", city.getLatitude())
                .queryParam("lng", city.getLongitude())
                .queryParam("date", date.toString())
                .build()
                .toUriString();
        SunriseSunsetResponseDTO responseDTO = this.webClient.get().uri(sunsetSunriseUrl).retrieve().bodyToMono(SunriseSunsetResponseDTO.class).block();
        if (responseDTO == null) {
            throw new NoSunriseSunsetDataException();
        }
        SunriseSunsetTime sunriseSunsetTime = convertToSunriseSunsetTime(responseDTO, date, city);
        return this.sunriseSunsetTimeRepository.save(sunriseSunsetTime);
    }


    /**
     * Converts a SunriseSunsetResponseDTO into a SunriseSunsetTime entity.
     *
     * @param responseDTO the DTO received from the external API
     * @param date the date associated with the data
     * @param city the city for which the data applies
     * @return a new SunriseSunsetTime entity
     */
    private SunriseSunsetTime convertToSunriseSunsetTime(SunriseSunsetResponseDTO responseDTO, LocalDate date, City city) {
        return new SunriseSunsetTime(responseDTO.results().sunrise(), responseDTO.results().sunset(), date, city);
    }


    /**
     * Converts a City entity to its corresponding CityDTO.
     *
     * @param city the City entity to convert
     * @return the CityDTO data transfer object
     */
    private CityDTO convertCityToCityDTO(City city) {
        return new CityDTO(
                city.getName(),
                city.getLatitude(),
                city.getLongitude(),
                city.getCountry(),
                city.getState()
        );
    }
}
