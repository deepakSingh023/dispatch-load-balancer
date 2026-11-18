package com.deepaksingh023.dispatch.controller;


import com.deepaksingh023.dispatch.dto.OrderSaveResponse;
import com.deepaksingh023.dispatch.dto.OrdersList;
import com.deepaksingh023.dispatch.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispatch")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderSaveResponse> saveOrders(
            @RequestBody @Valid OrdersList orders
            ){

        OrderSaveResponse response = orderService.saveOrders(orders);

        return ResponseEntity.ok().body(response);
    }
}
