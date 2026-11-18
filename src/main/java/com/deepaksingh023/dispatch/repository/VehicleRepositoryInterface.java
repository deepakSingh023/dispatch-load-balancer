package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Vehicle;

import java.util.List;

public interface VehicleRepositoryInterface {

    void bulkUpsert(List<Vehicle> orders);

}
