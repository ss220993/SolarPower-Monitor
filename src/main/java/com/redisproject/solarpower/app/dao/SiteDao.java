package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.Site;

import java.util.Set;

public interface SiteDao {
    void insert(Site site);
    Site findById(long id);
    Set<Site> findAll();
}
