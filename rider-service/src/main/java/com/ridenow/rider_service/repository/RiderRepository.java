package com.ridenow.rider_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ridenow.rider_service.entity.*;

import java.util.*;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    Optional<Rider> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByPhone(String phone);

    


}
