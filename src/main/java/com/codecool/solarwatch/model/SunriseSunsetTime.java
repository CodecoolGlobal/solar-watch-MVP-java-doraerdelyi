package com.codecool.solarwatch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "sunrise_sunset_times")
public class SunriseSunsetTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("sunrise")
    private Long sunriseTimeStamp;

    @JsonProperty("sunset")
    private Long sunsetTimeStamp;

    @OneToOne
    @JoinColumn(name = "city_id", nullable = false, unique = true)
    private City city;


    public SunriseSunsetTime() {}

    @Transient
    public LocalDateTime getSunriseTime() {
        return convertToLocalDateTime(sunriseTimeStamp);
    }

    @Transient
    public LocalDateTime getSunsetTime() {
        return convertToLocalDateTime(sunsetTimeStamp);
    }

    private LocalDateTime convertToLocalDateTime(Long timeStamp) {
        if (timeStamp == null) return null;
        return Instant.ofEpochSecond(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public Long getSunriseTimeStamp() {
        return sunriseTimeStamp;
    }

    public Long getSunsetTimeStamp() {
        return sunsetTimeStamp;
    }
}
