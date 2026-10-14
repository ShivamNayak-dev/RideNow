package com.ridenow.driver_service.service;

import com.ridenow.driver_service.dto.CreateDriverRequest;
import com.ridenow.driver_service.dto.DriverResponse;
import com.ridenow.driver_service.dto.UpdateDriverRequest;
import com.ridenow.driver_service.entity.Driver;
import com.ridenow.driver_service.enums.DriverStatus;
import com.ridenow.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverResponse createDriver(
            String userId, CreateDriverRequest request) {

        if (driverRepository.existsByUserId(userId)) {
            throw new IllegalStateException(
                    "Driver profile already exists for this user");
        }

        if (driverRepository.existsByLicenseNumber(
                request.licenseNumber())) {
            throw new IllegalStateException(
                    "License number is already registered");
        }

        Driver driver = new Driver(
                userId,
                request.name().trim(),
                request.phone().trim(),
                request.licenseNumber().trim());

        return toResponse(driverRepository.save(driver));
    }

    @Transactional(readOnly = true)
    public DriverResponse getMyProfile(String userId) {
        return toResponse(findByUserId(userId));
    }

    public DriverResponse updateMyProfile(
            String userId, UpdateDriverRequest request) {

        Driver driver = findByUserId(userId);
        driver.updateProfile(
                request.name().trim(),
                request.phone().trim());

        return toResponse(driverRepository.save(driver));
    }

    public DriverResponse goOnline(String userId) {
        Driver driver = findByUserId(userId);

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new IllegalStateException(
                    "Busy drivers cannot change availability");
        }

        driver.setStatus(DriverStatus.AVAILABLE);
        return toResponse(driverRepository.save(driver));
    }

    public DriverResponse goOffline(String userId) {
        Driver driver = findByUserId(userId);

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new IllegalStateException(
                    "Busy drivers cannot go offline");
        }

        driver.setStatus(DriverStatus.OFFLINE);
        return toResponse(driverRepository.save(driver));
    }

    private Driver findByUserId(String userId) {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Driver profile not found"));
    }

    private DriverResponse toResponse(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUserId(),
                driver.getName(),
                driver.getPhone(),
                driver.getLicenseNumber(),
                driver.getStatus(),
                driver.getRating(),
                driver.getCreatedAt(),
                driver.getUpdatedAt());
    }
}