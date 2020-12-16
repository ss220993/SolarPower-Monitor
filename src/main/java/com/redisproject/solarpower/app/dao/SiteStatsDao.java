package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.MeterReading;
import com.redisproject.solarpower.app.api.SiteStats;

import java.time.ZonedDateTime;

public interface SiteStatsDao {
    SiteStats findById(long siteId);
    SiteStats findById(long siteId, ZonedDateTime day);
    void update(MeterReading reading);
}
