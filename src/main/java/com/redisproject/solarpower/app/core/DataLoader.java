package com.redisproject.solarpower.app.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redisproject.solarpower.app.api.Site;
import com.redisproject.solarpower.app.dao.SiteDao;
import com.redisproject.solarpower.app.dao.SiteDaoRedisImpl;
import com.redisproject.solarpower.app.dao.SiteGeoDao;
import com.redisproject.solarpower.app.dao.SiteGeoDaoRedisImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataLoader {

    private final JedisPool jedisPool;
    private final InputStream inputStream;

    public DataLoader(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        this.inputStream = classloader.getResourceAsStream("data/sites.json");
    }

    public DataLoader(JedisPool jedisPool, String filename) throws FileNotFoundException {
        this.jedisPool = jedisPool;
        this.inputStream = new FileInputStream(filename);
    }

    public void load() throws IOException {
        System.out.println("Loading solar sites...");
        ObjectMapper mapper     = new ObjectMapper();
        List<Site>   sites      = mapper.readValue(inputStream, new TypeReference<List<Site>>(){});
        SiteDao      siteDao    = new SiteDaoRedisImpl(jedisPool);
        SiteGeoDao   siteGeoDao = new SiteGeoDaoRedisImpl(jedisPool);
        for (Site site : sites) {
            siteDao.insert(site);
            siteGeoDao.insert(site);
        }
    }

    public void flush() {
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println("Flushing database...");
            jedis.flushDB();
        }
    }
}
