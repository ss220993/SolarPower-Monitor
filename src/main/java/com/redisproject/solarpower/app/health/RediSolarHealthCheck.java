package com.redisproject.solarpower.app.health;

import com.codahale.metrics.health.HealthCheck;
import com.redisproject.solarpower.app.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RediSolarHealthCheck extends HealthCheck {

    private final RedisConfig redisConfig;

    public RediSolarHealthCheck(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Override
    protected Result check() {
        Jedis jedis;
        try {
            jedis = new Jedis(redisConfig.getHost(), redisConfig.getPort());
            jedis.ping();
        } catch (JedisConnectionException e) {
            return Result.unhealthy(e);
        }

        jedis.close();
        return Result.healthy();
    }
}
