package com.redisproject.solarpower.app.api;

public enum MetricUnit {
    WHGenerated("whG"),
    WHUsed("whU"),
    TemperatureCelsius("tempC");

    private final String shortName;

    MetricUnit(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
