package com.codecool.solarwatch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "sunrise_sunset_times")
public class SunriseSunsetTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("sunrise")
    private String sunriseTime;

    @JsonProperty("sunset")
    private String sunsetTime;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private LocalDate date;

    public SunriseSunsetTime() {}

    public SunriseSunsetTime(String sunriseTime, String sunsetTime, LocalDate date, City city) {
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.date = date;
        this.city = city;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }
}
