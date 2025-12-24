package com.example.aqi.service;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AqiService {

    @Value("${aqi.api.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ===== PUBLIC API =====
    @Cacheable(value = "aqiCache", key = "#city.toLowerCase()")
    public AqiResponse getAqiByCity(String city) {
        try {
            String url = "https://api.waqi.info/feed/" + city + "/?token=" + token;
            String json = restTemplate.getForObject(url, String.class);
            return parseResponse(json);
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch AQI data");
        }
    }

    // ===== INTERNAL LOGIC =====
    private AqiResponse parseResponse(String json) throws Exception {

        JsonNode data = objectMapper.readTree(json).path("data");

        int aqi = data.path("aqi").asInt();
        String city = data.path("city").path("name").asText();
        String dominantPollutant = data.path("dominentpol").asText();

        Map<String, Double> pollutants = new HashMap<>();
        data.path("iaqi").fields().forEachRemaining(e ->
            pollutants.put(e.getKey(), e.getValue().path("v").asDouble())
        );

        return new AqiResponse(
            city,
            aqi,
            dominantPollutant,
            healthAdvice(aqi),
            pollutants
        );
    }

    private String healthAdvice(int aqi) {
        if (aqi <= 50) return "Air quality is good. Enjoy outdoor activities.";
        if (aqi <= 100) return "Moderate air quality. Sensitive people take care.";
        if (aqi <= 200) return "Unhealthy air. Reduce outdoor exposure.";
        if (aqi <= 300) return "Very unhealthy. Avoid outdoor activities.";
        return "Hazardous air quality. Stay indoors.";
    }

    // ===== RESPONSE MODEL (INNER CLASS) =====
    public static class AqiResponse {
        public String city;
        public int aqi;
        public String dominantPollutant;
        public String healthAdvice;
        public Map<String, Double> pollutants;

        public AqiResponse(String city, int aqi, String dominantPollutant,
                           String healthAdvice, Map<String, Double> pollutants) {
            this.city = city;
            this.aqi = aqi;
            this.dominantPollutant = dominantPollutant;
            this.healthAdvice = healthAdvice;
            this.pollutants = pollutants;
        }
    }
}

