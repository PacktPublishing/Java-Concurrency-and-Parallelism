package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Data classes and interfaces
class WeatherStationReading {
    // Example fields
    private String stationId;
    private double temperature;

    // Constructor and getters
    public WeatherStationReading(String stationId, double temperature) {
        this.stationId = stationId;
        this.temperature = temperature;
    }

    public String getStationId() {
        return stationId;
    }

    public double getTemperature() {
        return temperature;
    }
}

class ProcessedWeatherData {
    // Simplistic representation
    private double averageTemperature;

    // Constructor and getter
    public ProcessedWeatherData(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }
}

interface WeatherDataProcessor {
    ProcessedWeatherData processWeatherData(List<WeatherStationReading> readings);
}

// Implementation classes
class Bulkhead {
    private final String region;
    private final List<WeatherDataProcessor> processors;

    public Bulkhead(String region, List<WeatherDataProcessor> processors) {
        this.region = region;
        this.processors = processors;
    }

    public ProcessedWeatherData processRegionalData(List<WeatherStationReading> readings) {
        List<ProcessedWeatherData> partialResults = new ArrayList<>();
        for (WeatherDataProcessor processor : processors) {
            partialResults.add(processor.processWeatherData(readings));
        }
        return mergeRegionalData(partialResults);
    }

    private ProcessedWeatherData mergeRegionalData(List<ProcessedWeatherData> partialResults) {
        // Simplistic merge logic (summing averages)
        double sum = 0;
        for (ProcessedWeatherData data : partialResults) {
            sum += data.getAverageTemperature();
        }
        return new ProcessedWeatherData(sum / partialResults.size());
    }
}

class WeatherDataCoordinator {
    private final Map<String, Bulkhead> bulkheads;

    public WeatherDataCoordinator(Map<String, Bulkhead> bulkheads) {
        this.bulkheads = bulkheads;
    }

    public ProcessedWeatherData processAllData(List<WeatherStationReading> readings) {
        Map<String, List<WeatherStationReading>> regionalData = groupDataByRegion(readings);
        Map<String, ProcessedWeatherData> regionalResults = new HashMap<>();
        for (String region : regionalData.keySet()) {
            regionalResults.put(region, bulkheads.get(region).processRegionalData(regionalData.get(region)));
        }
        return mergeGlobalData(regionalResults);
    }

    private Map<String, List<WeatherStationReading>> groupDataByRegion(List<WeatherStationReading> readings) {
        // Simplistic grouping logic by region ID
        Map<String, List<WeatherStationReading>> grouped = new HashMap<>();
        for (WeatherStationReading reading : readings) {
            grouped.computeIfAbsent(reading.getStationId(), k -> new ArrayList<>()).add(reading);
        }
        return grouped;
    }

    private ProcessedWeatherData mergeGlobalData(Map<String, ProcessedWeatherData> regionalResults) {
        // Simplistic global merge logic (average of averages)
        double sum = 0;
        for (ProcessedWeatherData data : regionalResults.values()) {
            sum += data.getAverageTemperature();
        }
        return new ProcessedWeatherData(sum / regionalResults.size());
    }
}
