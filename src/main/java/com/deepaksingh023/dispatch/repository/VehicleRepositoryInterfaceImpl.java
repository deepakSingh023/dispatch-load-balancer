package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Vehicle;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class VehicleRepositoryInterfaceImpl implements VehicleRepositoryInterface{

    private final JdbcTemplate jdbcTemplate;

    public VehicleRepositoryInterfaceImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void bulkUpsert(List<Vehicle> vehicles){

        String query = """
                INSERT INTO vehicle_table(vehicle_id, capacity, current_latitude, current_longitude, current_address)
                VALUES( ?, ?, ?, ?, ?)
                ON CONFLICT(vehicle_id)
                DO UPDATE SET
                   capacity = EXCLUDED.capacity,
                   current_latitude = EXCLUDED.current_latitude,
                   current_longitude = EXCLUDED.current_longitude,
                   current_address = EXCLUDED.current_address
                """;

        jdbcTemplate.batchUpdate(query,vehicles,500,(ps,vehicle)->{
            ps.setString(1,vehicle.getVehicleId());
            ps.setObject(2,vehicle.getCapacity());
            ps.setObject(3,vehicle.getCurrentLatitude());
            ps.setObject(4,vehicle.getCurrentLongitude());
            ps.setString(5,vehicle.getCurrentAddress());
        });
    }




}
