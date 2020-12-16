package com.redisproject.solarpower.app.dao;

import java.time.ZonedDateTime;

import com.redisproject.solarpower.app.core.KeyHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class RateLimiterSlidingDaoRedisImpl implements RateLimiter {

    private final JedisPool jedisPool;
    private final long windowSizeMS;
    private final long maxHits;

    public RateLimiterSlidingDaoRedisImpl(JedisPool pool, long windowSizeMS,
                                          long maxHits) {
        this.jedisPool = pool;
        this.windowSizeMS = windowSizeMS;
        this.maxHits = maxHits;
    }

    // Challenge #7
    @Override
    public void hit(String name) throws RateLimitExceededException {
        // START CHALLENGE #7
        try(Jedis jedis = jedisPool.getResource()){
            String key = KeyHelper.getKey("limiter:" + windowSizeMS + ":" + name + ":" + maxHits);
            long now = ZonedDateTime.now().toInstant().toEpochMilli();

            Transaction trans = jedis.multi();
            String keyValue = now + "-" + Math.random();
            trans.zadd(key, now, keyValue);
            trans.zremrangeByScore(key, 0, now - windowSizeMS);
            Response<Long> hits = trans.zcard(key);
            trans.exec();

            if (hits.get() > maxHits) {
                throw new RateLimitExceededException();
            }
        }
        // END CHALLENGE #7
    }
}
