package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String>,VehicleRepositoryInterface{

    @Query(value = "SELECT v FROM Vehicle v")
    List<Vehicle> getAllVehicles();
}
