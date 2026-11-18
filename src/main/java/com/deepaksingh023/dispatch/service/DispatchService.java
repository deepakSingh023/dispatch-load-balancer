package com.deepaksingh023.dispatch.service;


import com.deepaksingh023.dispatch.dto.DispatchPlan;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.error.exceptions.NoOrdersToProcess;
import com.deepaksingh023.dispatch.error.exceptions.NoVehicleToProcessOrders;
import com.deepaksingh023.dispatch.repository.OrderRepository;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import com.deepaksingh023.dispatch.util.HaversineFormula;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DispatchService {

    private final OrderRepository orderRepository;

    private final VehicleRepository vehicleRepository;

    private final HaversineFormula haversineFormula;

    public DispatchService(OrderRepository orderRepository, VehicleRepository vehicleRepository , HaversineFormula haversineFormula){
        this.haversineFormula = haversineFormula;
        this.orderRepository = orderRepository;
        this.vehicleRepository= vehicleRepository;
    }


    public DispatchPlan sendDispatchPlan(){

        List<Order>  orders = orderRepository.getUnassignedOrders();

        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();

        if(orders.isEmpty()){
            throw new NoOrdersToProcess("There are no orders to process");
        }
        if(vehicles.isEmpty()){
            throw new NoVehicleToProcessOrders("There is no vehicle to proces orders");
        }

        orders.sort(Comparator.comparingInt(a -> a.getPriority().getRank()));

        for(Order order : orders){

            String vehicleSelected;
            int minDistanceTravlled




        }





    }
}
