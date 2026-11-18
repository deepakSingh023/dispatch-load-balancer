package com.deepaksingh023.dispatch.service;


import com.deepaksingh023.dispatch.dto.VehicleList;
import com.deepaksingh023.dispatch.dto.VehicleRequest;
import com.deepaksingh023.dispatch.dto.VehicleSaveResponse;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.enums.Status;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateVehicleException;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }


    @Transactional
    public VehicleSaveResponse saveVehicles(VehicleList vehicleList){

        HashSet<String>  checkDuplicate = new HashSet<>();
        List<Vehicle> vehiclesToBeSaved = new ArrayList<>();

        for(VehicleRequest vehicleRequest : vehicleList.vehicles()){

            if(checkDuplicate.contains(vehicleRequest.vehicleId())){
                throw new DuplicateVehicleException("Duplicate vehicles");
            }

            Vehicle vehicle = Vehicle.builder()
                    .vehicleId(vehicleRequest.vehicleId())
                    .capacity(vehicleRequest.capacity())
                    .currentLatitude(vehicleRequest.currentLatitude())
                    .currentLongitude(vehicleRequest.currentLongitude())
                    .currentAddress(vehicleRequest.currentAddress())
                    .build();


            checkDuplicate.add(vehicleRequest.vehicleId());
            vehiclesToBeSaved.add(vehicle);
        }

        vehicleRepository.saveAll(vehiclesToBeSaved);

        return new VehicleSaveResponse("Vehicles saved successfully" , Status.SUCCESS);

    }

}
