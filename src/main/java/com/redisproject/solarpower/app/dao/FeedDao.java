package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.MeterReading;

import java.util.List;

public interface FeedDao {
    void insert(MeterReading meterReading);
    List<MeterReading> getRecentGlobal(int limit);
    List<MeterReading> getRecentForSite(long siteId, int limit);
}
