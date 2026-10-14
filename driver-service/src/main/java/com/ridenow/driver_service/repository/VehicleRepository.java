
package com.ridenow.driver_service.repository;

import com.ridenow.driver_service.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByDriver_Id(Long driverId);

    Optional<Vehicle> findByDriver_UserId(String userId);

    boolean existsByVehicleNumber(String vehicleNumber);

    boolean existsByDriver_Id(Long driverId);
}
