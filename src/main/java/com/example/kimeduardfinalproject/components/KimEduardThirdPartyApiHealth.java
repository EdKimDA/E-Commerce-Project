package com.example.kimeduardfinalproject.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KimEduardThirdPartyApiHealth implements HealthIndicator {
    private final RestTemplate template = new RestTemplate();

    @Override
    public Health health() {
        try {
            String thirdPartyApi = "https://api.github.com";

            ResponseEntity<String> response = template.getForEntity(thirdPartyApi, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Health.up().withDetail("gitHubApi", "Available").build();
            } else {
                return Health.down().withDetail("gitHubApi", "Unavailable").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("gihHubApi", "error").build();
        }
    }
}
