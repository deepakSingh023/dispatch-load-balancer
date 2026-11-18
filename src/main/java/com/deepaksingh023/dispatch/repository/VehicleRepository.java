package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,String> {

}
