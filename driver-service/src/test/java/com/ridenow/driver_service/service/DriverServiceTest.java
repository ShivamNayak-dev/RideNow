package com.ridenow.driver_service.service;

import com.ridenow.driver_service.dto.CreateDriverRequest;
import com.ridenow.driver_service.entity.Driver;
import com.ridenow.driver_service.enums.DriverStatus;
import com.ridenow.driver_service.exception.BusinessRuleException;
import com.ridenow.driver_service.exception.ResourceConflictException;
import com.ridenow.driver_service.exception.ResourceNotFoundException;
import com.ridenow.driver_service.repository.DriverRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = new Driver(
                "user-101",
                "Test Driver",
                "9876543210",
                "DL-TEST-1001");
    }

    @Test
    void shouldCreateDriverProfile() {
        CreateDriverRequest request = new CreateDriverRequest(
                "Test Driver",
                "9876543210",
                "DL-TEST-1001");

        when(driverRepository.existsByUserId("user-101"))
                .thenReturn(false);
        when(driverRepository.existsByLicenseNumber("DL-TEST-1001"))
                .thenReturn(false);
        when(driverRepository.save(any(Driver.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = driverService.createDriver("user-101", request);

        assertEquals("Test Driver", response.name());
        assertEquals(DriverStatus.OFFLINE, response.status());
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void shouldRejectDuplicateDriverProfile() {
        when(driverRepository.existsByUserId("user-101"))
                .thenReturn(true);

        assertThrows(ResourceConflictException.class,
                () -> driverService.createDriver(
                        "user-101",
                        new CreateDriverRequest(
                                "Test Driver",
                                "9876543210",
                                "DL-TEST-1001")));
    }

    @Test
    void shouldGoOnline() {
        when(driverRepository.findByUserId("user-101"))
                .thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = driverService.goOnline("user-101");

        assertEquals(DriverStatus.AVAILABLE, response.status());
    }

    @Test
    void shouldRejectGoingOfflineWhenBusy() {
        driver.setStatus(DriverStatus.BUSY);

        when(driverRepository.findByUserId("user-101"))
                .thenReturn(Optional.of(driver));

        assertThrows(BusinessRuleException.class,
                () -> driverService.goOffline("user-101"));

        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void shouldReturnNotFoundForMissingDriver() {
        when(driverRepository.findByUserId("unknown-user"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> driverService.getMyProfile("unknown-user"));
    }
}