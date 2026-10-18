
package com.ridenow.location_service.service;

import com.ridenow.location_service.dto.LocationResponse;
import com.ridenow.location_service.dto.LocationUpdateRequest;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class LocationService {

    private static final String GEO_KEY = "drivers:geo";
    private static final String LOCATION_KEY_PREFIX = "location:driver:";
    private static final Duration LOCATION_TTL = Duration.ofSeconds(90);

    private final StringRedisTemplate redisTemplate;

    public LocationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public LocationResponse updateLocation(
            String driverId,
            LocationUpdateRequest request) {

        Instant updatedAt = Instant.now();

        String locationKey = LOCATION_KEY_PREFIX + driverId;

        // Store the latest coordinates and timestamp.
        redisTemplate.opsForHash().putAll(
                locationKey,
                Map.of(
                        "latitude", request.getLatitude().toString(),
                        "longitude", request.getLongitude().toString(),
                        "updatedAt", updatedAt.toString()
                )
        );

        // Expire the location record if GPS updates stop.
        redisTemplate.expire(locationKey, LOCATION_TTL);

        // Redis GEO uses longitude as X and latitude as Y.
        redisTemplate.opsForGeo().add(
                GEO_KEY,
                new Point(
                        request.getLongitude(),
                        request.getLatitude()
                ),
                driverId
        );

        return new LocationResponse(
                driverId,
                request.getLatitude(),
                request.getLongitude(),
                updatedAt
        );
    }

    public Optional<LocationResponse> getLocation(String driverId) {

        String locationKey = LOCATION_KEY_PREFIX + driverId;

        Map<Object, Object> data =
                redisTemplate.opsForHash().entries(locationKey);

        if (data.isEmpty()) {
            // GEO members don't expire individually.
            // Remove the member if its location record has expired.
            redisTemplate.opsForGeo().remove(GEO_KEY, driverId);
            return Optional.empty();
        }

        return Optional.of(
                new LocationResponse(
                        driverId,
                        Double.valueOf(data.get("latitude").toString()),
                        Double.valueOf(data.get("longitude").toString()),
                        Instant.parse(data.get("updatedAt").toString())
                )
        );
    }
}
