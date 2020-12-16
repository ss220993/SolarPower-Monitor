package com.redisproject.solarpower.app;

import com.redisproject.solarpower.app.command.LoadCommand;
import com.redisproject.solarpower.app.command.RunCommand;
import com.redislabs.university.RU102J.dao.*;
import com.redisproject.solarpower.app.dao.CapacityDaoRedisImpl;
import com.redisproject.solarpower.app.dao.FeedDaoRedisImpl;
import com.redisproject.solarpower.app.dao.MetricDaoRedisZsetImpl;
import com.redisproject.solarpower.app.health.RediSolarHealthCheck;
import com.redislabs.university.RU102J.resources.*;
import com.redisproject.solarpower.app.dao.SiteGeoDaoRedisImpl;
import com.redisproject.solarpower.app.dao.SiteStatsDaoRedisImpl;
import com.redisproject.solarpower.app.resources.CapacityResource;
import com.redisproject.solarpower.app.resources.MeterReadingResource;
import com.redisproject.solarpower.app.resources.MetricsResource;
import com.redisproject.solarpower.app.resources.SiteGeoResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RediSolarApplication extends Application<RediSolarConfiguration> {

    public static void main(final String[] args) throws Exception {
        new RediSolarApplication().run(args);
    }

    @Override
    public String getName() {
        return "RediSolar";
    }

    @Override
    public void initialize(final Bootstrap<RediSolarConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/dashboard/dist", "/", "index.html"));
        bootstrap.addCommand(new LoadCommand());
        bootstrap.addCommand(new RunCommand());
    }

    @Override
    public void run(final RediSolarConfiguration configuration,
                    final Environment environment) {
        RedisConfig redisConfig = configuration.getRedisConfig();
        JedisPool jedisPool;
        
        String password = redisConfig.getPassword();

        if (password.length() > 0) {
                jedisPool = new JedisPool(new JedisPoolConfig(), redisConfig.getHost(),
                redisConfig.getPort(), 2000, redisConfig.getPassword());
        } else {
                jedisPool = new JedisPool(redisConfig.getHost(), redisConfig.getPort());
        }

        // To use the geospatial features, replace the following lines with:
         SiteGeoResource siteResource =
                new SiteGeoResource(new SiteGeoDaoRedisImpl(jedisPool));
//        SiteResource siteResource =
//                new SiteResource(new SiteDaoRedisImpl(jedisPool));
        environment.jersey().register(siteResource);

        // For RedisTimeSeries: replace the next lines with
        // MetricsResource metricsResource =
        //              new MetricsResource(new MetricDaoRedisTSImpl(jedisPool));
                MetricsResource metricsResource =
                        new MetricsResource(new MetricDaoRedisZsetImpl(jedisPool));
        environment.jersey().register(metricsResource);

        CapacityResource capacityResource =
                new CapacityResource(new CapacityDaoRedisImpl(jedisPool));
        environment.jersey().register(capacityResource);

        MeterReadingResource meterResource =
                new MeterReadingResource(new SiteStatsDaoRedisImpl(jedisPool),
                        new MetricDaoRedisZsetImpl(jedisPool),
                        // For RedisTimeSeries: new MetricDaoRedisTSImpl(jedisPool),
                        new CapacityDaoRedisImpl(jedisPool),
                        new FeedDaoRedisImpl(jedisPool));
        environment.jersey().register(meterResource);

        RediSolarHealthCheck healthCheck = new RediSolarHealthCheck(redisConfig);
        environment.healthChecks().register("healthy", healthCheck);
    }
}
