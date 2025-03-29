package com.codecool.solarwatch;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolarWatchApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().load();
        SpringApplication.run(SolarWatchApplication.class, args);
    }

}
