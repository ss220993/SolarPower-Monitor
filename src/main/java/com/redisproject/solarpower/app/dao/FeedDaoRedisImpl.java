package com.redisproject.solarpower.app.dao;

import com.redisproject.solarpower.app.api.MeterReading;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

public class FeedDaoRedisImpl implements FeedDao {

    private final JedisPool jedisPool;
    private static final long globalMaxFeedLength = 10000;
    private static final long siteMaxFeedLength = 2440;

    public FeedDaoRedisImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // Challenge #6
    @Override
    public void insert(MeterReading meterReading) {
        // START Challenge #6
        try (Jedis jedis = jedisPool.getResource()) {
            String globalFeedKey = RedisSchema.getGlobalFeedKey();
            String siteFeedKey   = RedisSchema.getFeedKey(meterReading.getSiteId());

            Pipeline pipe = jedis.pipelined();
            pipe.xadd(globalFeedKey, StreamEntryID.NEW_ENTRY, meterReading.toMap(), globalMaxFeedLength, true);
            pipe.xadd(siteFeedKey, StreamEntryID.NEW_ENTRY, meterReading.toMap(), siteMaxFeedLength, true);
            pipe.sync();
        }
        // END Challenge #6
    }

    @Override
    public List<MeterReading> getRecentGlobal(int limit) {
        return getRecent(RedisSchema.getGlobalFeedKey(), limit);
    }

    @Override
    public List<MeterReading> getRecentForSite(long siteId, int limit) {
        return getRecent(RedisSchema.getFeedKey(siteId), limit);
    }

    public List<MeterReading> getRecent(String key, int limit) {
        List<MeterReading> readings = new ArrayList<>(limit);
        try (Jedis jedis = jedisPool.getResource()) {
            List<StreamEntry> entries = jedis.xrevrange(key, null,
                    null, limit);
            for (StreamEntry entry : entries) {
                readings.add(new MeterReading(entry.getFields()));
            }
            return readings;
        }
    }
}
