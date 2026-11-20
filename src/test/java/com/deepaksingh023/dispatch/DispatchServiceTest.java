package com.deepaksingh023.dispatch;

import com.deepaksingh023.dispatch.dto.FullResponse;
import com.deepaksingh023.dispatch.dto.OrderRequest;
import com.deepaksingh023.dispatch.dto.PlanResponse;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.dto.VehicleRequest;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.enums.Priority;
import com.deepaksingh023.dispatch.error.exceptions.NoOrdersToProcess;
import com.deepaksingh023.dispatch.error.exceptions.NoVehicleToProcessOrders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    DispatchService dispatchService;

    @BeforeEach
    void setUp() {
        dispatchService = new DispatchService(
                orderRepository, vehicleRepository, new HaversineFormula());
    }


    @Test
    void shouldThrowExceptionWhenNoOrdersButThereAreVehicles(){

        Vehicle vehicle = new Vehicle(
                "V1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        Vehicle vehicle2 = new Vehicle(
                "V2",
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
                "V1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        Vehicle vehicle2 = new Vehicle(
                "V2",
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

        assertEquals("V1",plan.dispatchPlan().get(0).vehicleId());
        assertEquals("V2",plan.dispatchPlan().get(1).vehicleId());

        assertEquals("O1",plan.dispatchPlan().get(0).assignedOrders().orders().get(0).orderId());
        assertEquals("O2",plan.dispatchPlan().get(1).assignedOrders().orders().get(0).orderId());

        assertEquals(20,plan.dispatchPlan().get(0).totalLoad());

        assertEquals(20,plan.dispatchPlan().get(1).totalLoad());

        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
        Mockito.verify(vehicleRepository,Mockito.times(1)).getAllVehicles();

    }

    // If there are identical vehcile distance then alwasy choose the first one in list
    @Test
    void ifIdenticalVehicleDataThenTheFirstOnceGetOrder(){


        Vehicle vehicle = new Vehicle(
                "V1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        Vehicle vehicle2 = new Vehicle(
                "V2",
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

        List<Order> orders= new ArrayList<>(List.of(order));


        when(orderRepository.findAll()).thenReturn(orders);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);

        FullResponse response = dispatchService.sendDispatchPlan();

        Assertions.assertNotNull(response);

        Assertions.assertEquals(1,response.dispatchPlan().get(0).assignedOrders().orders().size());

        Assertions.assertEquals(0,response.dispatchPlan().get(1).assignedOrders().orders().size());

        Assertions.assertEquals(0,response.unassignedOrders().size());

        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
        Mockito.verify(vehicleRepository,Mockito.times(1)).getAllVehicles();


    }


    //Check if only the near vehicle gets the order not the far once
    @Test
    void theNearVehicleShouldGetTheOrderTest(){


        Vehicle vehicle = new Vehicle(
                "V1",
                20,
                26.4499,
                80.3319,
                "Kanpur"

        );
        Vehicle vehicle2 = new Vehicle(
                "V2",
                20,
                28.6791,
                77.0697,
                "New Delhi"

        );

        List<Vehicle> vehicles = new ArrayList<>(List.of(vehicle,vehicle2));

        Order order = new Order(
                "O1",
                28.5355,
                77.3910,
                "Noida",
                20,
                Priority.HIGH
        );

        List<Order> orders= new ArrayList<>(List.of(order));

        when(orderRepository.findAll()).thenReturn(orders);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);

        FullResponse response = dispatchService.sendDispatchPlan();

        Assertions.assertNotNull(response);

        Assertions.assertEquals(1 , response.dispatchPlan().get(1).assignedOrders().orders().size());


        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
        Mockito.verify(vehicleRepository,Mockito.times(1)).getAllVehicles();


    }

    //test to check if higher priority one gets the order
    @Test
    void onlyTheHighPriorityShouldGetTheSpaceTest(){


        Order order = new Order(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.LOW
        );
        Order order2 = new Order(
                "O2",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );

        Vehicle vehicle2 = new Vehicle(
                "V2",
                20,
                28.6791,
                77.0697,
                "New Delhi"

        );

        List<Vehicle> vehicles = new ArrayList<>(List.of(vehicle2));

        List<Order> orders= new ArrayList<>(List.of(order,order2));

        when(orderRepository.findAll()).thenReturn(orders);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);

        FullResponse response = dispatchService.sendDispatchPlan();


        assertNotNull(response);

        assertEquals("O2",response.dispatchPlan().get(0).assignedOrders().orders().get(0).orderId());

        assertEquals(Priority.HIGH,response.dispatchPlan().get(0).assignedOrders().orders().get(0).priority());

        assertEquals("O1",response.unassignedOrders().get(0).orderId());


    }

    //Test to check if the order still gets assigned with heavier capacity
    @Test
    void orderWithHeavyLoadThanCcapacityTest(){

        Order order = new Order(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                21,
                Priority.HIGH
        );

        Vehicle vehicle2 = new Vehicle(
                "V2",
                20,
                28.6791,
                77.0697,
                "New Delhi"

        );

        List<Vehicle> vehicles = new ArrayList<>(List.of(vehicle2));

        List<Order> orders= new ArrayList<>(List.of(order));

        when(orderRepository.findAll()).thenReturn(orders);

        when(vehicleRepository.getAllVehicles()).thenReturn(vehicles);

        FullResponse response = dispatchService.sendDispatchPlan();

        assertEquals("O1",response.unassignedOrders().get(0).orderId());

        assertEquals(0,response.dispatchPlan().get(0).totalLoad());


    }



}
