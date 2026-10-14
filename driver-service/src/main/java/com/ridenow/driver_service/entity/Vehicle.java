
package com.ridenow.driver_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vehicle_driver_id",
                        columnNames = "driver_id"
                ),
                @UniqueConstraint(
                        name = "uk_vehicle_registration_number",
                        columnNames = "vehicle_number"
                )
        }
)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "driver_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehicle_driver")
    )
    private Driver driver;

    @Column(name = "vehicle_number", nullable = false, length = 20)
    private String vehicleNumber;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "vehicle_color", nullable = false, length = 30)
    private String vehicleColor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Vehicle() {
    }

    public Vehicle(Driver driver, String vehicleNumber, String type,
                   String model, String vehicleColor) {
        this.driver = driver;
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.model = model;
        this.vehicleColor = vehicleColor;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateVehicle(String vehicleNumber, String type,
                              String model, String vehicleColor) {
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.model = model;
        this.vehicleColor = vehicleColor;
    }
}
