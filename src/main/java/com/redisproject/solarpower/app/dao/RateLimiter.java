package com.redisproject.solarpower.app.dao;

public interface RateLimiter {
    void hit(String name) throws RateLimitExceededException;
}
