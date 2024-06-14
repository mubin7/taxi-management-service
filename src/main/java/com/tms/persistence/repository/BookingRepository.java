package com.tms.persistence.repository;

import com.tms.constant.JourneyStatus;
import com.tms.persistence.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByJourneyStatus(JourneyStatus journeyStatus);

    List<Booking> findByJourneyStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
