package com.tms.persistence.repository;

import com.tms.constant.TaxiStatus;
import com.tms.persistence.entity.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, String> {

    List<Taxi> findByTaxiStatus(TaxiStatus taxiStatus);
}
