package com.ridenow.rider_service.service;

import com.ridenow.rider_service.dto.CreateRiderRequest;
import com.ridenow.rider_service.dto.UpdateRiderRequest;
import com.ridenow.rider_service.entity.Rider;
import com.ridenow.rider_service.exception.RiderNotFoundException;
import com.ridenow.rider_service.exception.ResourceConflictException;
import com.ridenow.rider_service.repository.RiderRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Transactional
    public Rider createRider(
            String userId,
            CreateRiderRequest request) {

        if (riderRepository.existsByUserId(userId)) {
            throw new ResourceConflictException(
                    "Rider profile already exists");
        }

        if (riderRepository.existsByPhone(request.phone())) {
            throw new ResourceConflictException(
                    "Phone number already exists");
        }

        Rider rider = new Rider();
        rider.setUserId(userId);
        rider.setName(request.name());
        rider.setPhone(request.phone());
        rider.setEmail(request.email());

        try {
            return riderRepository.saveAndFlush(rider);
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException(
                    "Rider profile or phone number already exists");
        }
    }

    @Transactional(readOnly = true)
    public Rider getRider(String userId) {
        return riderRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RiderNotFoundException(
                                "Rider profile not found"));
    }

    @Transactional
    public Rider updateRider(
            String userId,
            UpdateRiderRequest request) {

        Rider rider = getRider(userId);

        if (riderRepository.existsByPhone(request.phone())
                && !rider.getPhone().equals(request.phone())) {
            throw new ResourceConflictException(
                    "Phone number already exists");
        }

        rider.setName(request.name());
        rider.setPhone(request.phone());
        rider.setEmail(request.email());

        try {
            return riderRepository.saveAndFlush(rider);
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException(
                    "Phone number already exists");
        }
    }
}