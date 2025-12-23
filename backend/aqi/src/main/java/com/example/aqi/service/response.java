package com.example.aqi.service;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class response {
    private String city;
    private int aqi;
    private String dominantPollutant;
    private String healthAdvice;
    private Map<String, Double> pollutants;
}
