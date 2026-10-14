package com.ridenow.driver_service.service;

import com.ridenow.driver_service.dto.CreateVehicleRequest;
import com.ridenow.driver_service.dto.UpdateVehicleRequest;
import com.ridenow.driver_service.dto.VehicleResponse;
import com.ridenow.driver_service.entity.Driver;
import com.ridenow.driver_service.entity.Vehicle;
import com.ridenow.driver_service.exception.ResourceConflictException;
import com.ridenow.driver_service.exception.ResourceNotFoundException;
import com.ridenow.driver_service.repository.DriverRepository;
import com.ridenow.driver_service.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleService(
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createMyVehicle(
            String userId, CreateVehicleRequest request) {

        Driver driver = findDriver(userId);

        if (vehicleRepository.existsByDriver_Id(driver.getId())) {
            throw new ResourceConflictException(
                    "This driver already has a registered vehicle");
        }

        String vehicleNumber = request.vehicleNumber().trim();

        if (vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
            throw new ResourceConflictException(
                    "Vehicle registration number is already registered");
        }

        Vehicle vehicle = new Vehicle(
                driver,
                vehicleNumber,
                request.type().trim(),
                request.model().trim(),
                request.vehicleColor().trim());

        return toResponse(vehicleRepository.save(vehicle));
    }

    @Transactional(readOnly = true)
    public VehicleResponse getMyVehicle(String userId) {
        Vehicle vehicle = vehicleRepository.findByDriver_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found for this driver"));

        return toResponse(vehicle);
    }

    public VehicleResponse updateMyVehicle(
            String userId, UpdateVehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findByDriver_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found for this driver"));

        String vehicleNumber = request.vehicleNumber().trim();

        if (!vehicle.getVehicleNumber().equalsIgnoreCase(vehicleNumber)
                && vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
            throw new ResourceConflictException(
                    "Vehicle registration number is already registered");
        }

        vehicle.updateVehicle(
                vehicleNumber,
                request.type().trim(),
                request.model().trim(),
                request.vehicleColor().trim());

        return toResponse(vehicleRepository.save(vehicle));
    }

    private Driver findDriver(String userId) {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver profile not found"));
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getDriver().getId(),
                vehicle.getVehicleNumber(),
                vehicle.getType(),
                vehicle.getModel(),
                vehicle.getVehicleColor(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt());
    }
}