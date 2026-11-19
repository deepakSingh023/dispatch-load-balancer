package com.deepaksingh023.dispatch;

import com.deepaksingh023.dispatch.dto.FullResponse;
import com.deepaksingh023.dispatch.dto.OrderRequest;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.dto.VehicleRequest;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.enums.Priority;
import com.deepaksingh023.dispatch.error.exceptions.NoOrdersToProcess;
import com.deepaksingh023.dispatch.error.exceptions.NoVehicleToProcessOrders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deepaksingh023.dispatch.repository.OrderRepository;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import com.deepaksingh023.dispatch.service.DispatchService;
import com.deepaksingh023.dispatch.util.HaversineFormula;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DispatchServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    HaversineFormula haversineFormula;

    @InjectMocks
    DispatchService dispatchService;


    @Test
    void shouldThrowExceptionWhenNoOrdersButThereAreVehicles(){

        Vehicle vehicle = new Vehicle(
                "O1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        Vehicle vehicle2 = new Vehicle(
                "O2",
                20,
                27.0,
                56.0,
                "New Delhi"

        );

        List<Vehicle> vehicles = List.of(vehicle,vehicle2);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);
        when(orderRepository.findAll()).thenReturn(List.of());


        assertThrows(NoOrdersToProcess.class,() -> dispatchService.sendDispatchPlan());

    }

    @Test
    void shouldThrowExceptionWhenNoVehiclesButThereAreOrders(){

        Order order = new Order(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );
        Order order2 = new Order(
                "O2",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );

        List<Order> orders= List.of(order,order2);

        when(vehicleRepository.getAllVehicles()).thenReturn(List.of());
        when(orderRepository.findAll()).thenReturn(orders);


        assertThrows(NoVehicleToProcessOrders.class,() -> dispatchService.sendDispatchPlan());

    }

    @Test
    void shouldWorkNormallyWithoutException(){

        Vehicle vehicle = new Vehicle(
                "O1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        Vehicle vehicle2 = new Vehicle(
                "O2",
                20,
                27.0,
                56.0,
                "New Delhi"

        );

        List<Vehicle> vehicles = new ArrayList<>(List.of(vehicle,vehicle2));

        Order order = new Order(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );
        Order order2 = new Order(
                "O2",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );

        List<Order> orders= new ArrayList<>(List.of(order,order2));

        when(orderRepository.findAll()).thenReturn(orders);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);

        FullResponse plan = dispatchService.sendDispatchPlan();

        assertNotNull(plan);

        assertEquals(2 ,plan.dispatchPlan().size());


        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
        Mockito.verify(vehicleRepository,Mockito.times(1)).getAllVehicles();

    }

}
