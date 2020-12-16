package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.Measurement;
import com.redisproject.solarpower.app.api.MeterReading;
import com.redisproject.solarpower.app.api.MetricUnit;

import java.time.ZonedDateTime;
import java.util.List;

public interface MetricDao {
    void insert(MeterReading reading);
    List<Measurement> getRecent(Long siteId, MetricUnit unit,
                                ZonedDateTime time, Integer limit);
}
