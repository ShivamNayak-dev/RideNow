
package com.ridenow.location_service.dto;

import java.time.Instant;

public class LocationResponse {

    private String driverId;
    private Double latitude;
    private Double longitude;
    private Instant updatedAt;

    public LocationResponse() {
    }

    public LocationResponse(
            String driverId,
            Double latitude,
            Double longitude,
            Instant updatedAt) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = updatedAt;
    }

    public String getDriverId() {
        return driverId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
