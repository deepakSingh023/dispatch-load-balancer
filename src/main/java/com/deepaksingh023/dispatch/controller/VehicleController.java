package com.deepaksingh023.dispatch.controller;


import com.deepaksingh023.dispatch.dto.VehicleList;
import com.deepaksingh023.dispatch.dto.VehicleSaveResponse;
import com.deepaksingh023.dispatch.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispatch")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService=vehicleService;
    }


    @PostMapping("/vehicles")
    public ResponseEntity<VehicleSaveResponse> saveVehicles(
            @RequestBody @Valid VehicleList vehicleList
            ){

        VehicleSaveResponse vehicleSaveResponse = vehicleService.saveVehicles(vehicleList);

        return ResponseEntity.ok(vehicleSaveResponse);

    }

}
