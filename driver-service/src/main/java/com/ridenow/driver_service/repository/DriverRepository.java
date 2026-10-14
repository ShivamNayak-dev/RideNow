
package com.ridenow.driver_service.repository;

import com.ridenow.driver_service.entity.Driver;
import com.ridenow.driver_service.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Driver> findByStatus(DriverStatus status);
}
