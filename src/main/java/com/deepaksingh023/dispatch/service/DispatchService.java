package com.deepaksingh023.dispatch.service;


import com.deepaksingh023.dispatch.dto.*;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.error.exceptions.NoOrdersToProcess;
import com.deepaksingh023.dispatch.error.exceptions.NoVehicleToProcessOrders;
import com.deepaksingh023.dispatch.repository.OrderRepository;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import com.deepaksingh023.dispatch.util.HaversineFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Slf4j
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



    @Transactional(readOnly = true)
    public FullResponse sendDispatchPlan(){

        List<Order>  orders = orderRepository.findAll();

        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();

        if(orders.isEmpty()){
            throw new NoOrdersToProcess("There are no orders to process");
        }
        if(vehicles.isEmpty()){
            throw new NoVehicleToProcessOrders("There is no vehicle to proces orders");
        }

        log.info("Total no of Orders = {} and vehicles = {}" , orders.size(), vehicles.size());

        //Sorting for higher priority
        orders.sort(Comparator.comparingInt((Order o) -> o.getPriority().getRank()).thenComparing(Order :: getOrderId));

        //Using hashmap to access data by id so that it can be mutated
        HashMap<String,ResultCalculation> lookUp = new HashMap<>();

        List<UnAssignedOrderResponse> unassignedOrders = new ArrayList<>();

        //Creating a list of intermediate object to put the data on which can be converted to record for response
        for(Vehicle v : vehicles){
            List<OrderResponse> orderResponses = new ArrayList<>();

            AssignedOrders assignOrders = new AssignedOrders(orderResponses);

             ResultCalculation resultCalculation = new ResultCalculation(
                    v.getVehicleId(),
                    0,
                    0.0,
                    assignOrders,
                    v.getCurrentLatitude(),
                    v.getCurrentLongitude()
            );

            lookUp.put(v.getVehicleId(),resultCalculation);

        }

        for(Order order : orders){

            String selectedVehicle = null;
            Double distanceTravel = Double.MAX_VALUE;

            String response = null;


            for(Vehicle vehicle : vehicles){

                if(vehicle.getCapacity() - lookUp.get(vehicle.getVehicleId()).getTotalLoad() < order.getPackageWeight()){
                    response = "No Capacity left";
                    continue;
                }

                Double distanceTravelled = haversineFormula.calculateDistance(lookUp.get(vehicle.getVehicleId()).getCurrentLat(),lookUp.get(vehicle.getVehicleId()).getCurrentLon(),order.getLatitude(),order.getLongitude());


                //So only the vehicle selection if the distance from current vehicle is lower than the distance from prev vehicle otherwise continue
                if(distanceTravel <= distanceTravelled){
                    continue;
                }

                distanceTravel = distanceTravelled;
                selectedVehicle = vehicle.getVehicleId();


            }


            //If we have selected a vehicle then crete the dto for order and insert it into the assigned list of that vehicle
            if(selectedVehicle != null){
                OrderResponse orderResponse = new OrderResponse(
                        order.getOrderId(),
                        order.getLatitude(),
                        order.getLongitude(),
                        order.getAddress(),
                        order.getPackageWeight(),
                        order.getPriority()
                );
                ResultCalculation calculation = lookUp.get(selectedVehicle);

                calculation.setCurrentLat(order.getLatitude());
                calculation.setCurrentLon(order.getLongitude());
                calculation.setTotalDistanceTravelled(calculation.getTotalDistanceTravelled() + distanceTravel);
                calculation.setTotalLoad(calculation.getTotalLoad() + order.getPackageWeight());
                calculation.getAssignedOrders().orders().add(orderResponse);

                continue;
            }

            //No vehicle found so put it in unassigned orders
            UnAssignedOrderResponse orderResponse = new UnAssignedOrderResponse(
                    order.getOrderId(),
                    order.getLatitude(),
                    order.getLongitude(),
                    order.getAddress(),
                    order.getPackageWeight(),
                    order.getPriority(),
                    response
            );
            unassignedOrders.add(orderResponse);

        }

        log.info("Plan completed successfully with Assigned orders : {} and Unassigned orders : {}", orders.size() - unassignedOrders.size() , unassignedOrders);

        List<PlanResponse> responses = new ArrayList<>();

        for(Vehicle v : vehicles){
           ResultCalculation result = lookUp.get(v.getVehicleId());

            double num = result.getTotalDistanceTravelled();
            BigDecimal bd = BigDecimal.valueOf(num).setScale(2, RoundingMode.HALF_UP);

            double rounded = bd.doubleValue();

            PlanResponse planResponse = new PlanResponse(
                  result.getVehicleId(),
                  result.getTotalLoad(),
                  rounded,
                  result.getAssignedOrders()
            );

            responses.add(planResponse);
        }

        return new FullResponse(responses,unassignedOrders);

    }
}
