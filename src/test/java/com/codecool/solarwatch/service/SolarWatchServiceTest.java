
package com.codecool.solarwatch.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;


@ExtendWith(MockitoExtension.class)
public class SolarWatchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SolarWatchService solarWatchService;

}
