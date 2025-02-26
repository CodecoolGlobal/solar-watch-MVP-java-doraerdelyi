package com.codecool.solarwatch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("lat")
    private double latitude;

    @Column(nullable = false)
    @JsonProperty("lon")
    private double longitude;

    @Column(nullable = false)
    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;


    public City() {}

    public City(String name, double latitude, double longitude, String country, String state) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }
}
