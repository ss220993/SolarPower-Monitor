package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.GeoQuery;
import com.redisproject.solarpower.app.api.Site;

import java.util.Set;

public interface SiteGeoDao extends SiteDao {
    Set<Site> findByGeo(GeoQuery query);
}
