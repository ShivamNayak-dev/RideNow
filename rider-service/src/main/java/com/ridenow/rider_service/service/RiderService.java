package com.ridenow.rider_service.service;

import com.ridenow.rider_service.dto.CreateRiderRequest;
import com.ridenow.rider_service.dto.UpdateRiderRequest;
import com.ridenow.rider_service.entity.Rider;
import com.ridenow.rider_service.repository.RiderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Transactional
    public Rider createRider(String userId, CreateRiderRequest request) {

        if (riderRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Rider profile already exists");
        }

        if (riderRepository.existsByPhone(request.phone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Phone number already exists");
        }

        Rider rider = new Rider();
        rider.setUserId(userId);
        rider.setName(request.name());
        rider.setPhone(request.phone());
        rider.setEmail(request.email());

        return riderRepository.save(rider);
    }

    public Rider getRider(String userId) {
        return riderRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rider profile not found"));
    }

    @Transactional
    public Rider updateRider(String userId, UpdateRiderRequest request) {

        Rider rider = getRider(userId);

        boolean phoneUsedByAnotherRider =
                riderRepository.existsByPhone(request.phone())
                        && !rider.getPhone().equals(request.phone());

        if (phoneUsedByAnotherRider) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Phone number already exists");
        }

        rider.setName(request.name());
        rider.setPhone(request.phone());
        rider.setEmail(request.email());

        return riderRepository.save(rider);
    }
}