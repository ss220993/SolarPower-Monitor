package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.CapacityReport;
import com.redisproject.solarpower.app.api.MeterReading;

public interface CapacityDao {
    void update(MeterReading reading);
    CapacityReport getReport(Integer limit);
    Long getRank(Long siteId);
}
