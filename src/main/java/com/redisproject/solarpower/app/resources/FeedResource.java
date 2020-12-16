package com.redisproject.solarpower.app.resources;

import com.redisproject.solarpower.app.api.MeterReading;
import com.redisproject.solarpower.app.dao.FeedDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/capacity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeedResource {

    private final        FeedDao feedDao;
    private static final Integer defaultLimit = 20;

    public FeedResource(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    @GET
    public Response getAllEntries(@QueryParam("limit") Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        List<MeterReading> readings = feedDao.getRecentGlobal(limit);
        return Response.ok(readings)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getSingleFeed(@PathParam("id") Long siteId,
                                            @QueryParam("limit") Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        List<MeterReading> readings = feedDao.getRecentForSite(siteId, limit);
        return Response.ok(readings)
                .build();
    }
}
