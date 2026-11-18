package com.deepaksingh023.dispatch.service;


import com.deepaksingh023.dispatch.dto.OrderRequest;
import com.deepaksingh023.dispatch.dto.OrderSaveResponse;
import com.deepaksingh023.dispatch.dto.OrdersList;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.enums.OrderStatus;
import com.deepaksingh023.dispatch.enums.Status;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateOrderIdException;
import com.deepaksingh023.dispatch.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderSaveResponse saveOrders(OrdersList orderList){

        HashSet<String> duplicateCheckingSet = new HashSet<>();
        List<Order> ordersToBeSaved = new ArrayList<>();


        for(OrderRequest orderRequest : orderList.orders()){

            if(duplicateCheckingSet.contains(orderRequest.orderId())){
                throw new DuplicateOrderIdException("Duplicate order Ids");
            }

            Order order = Order.builder()
                    .orderId(orderRequest.orderId())
                    .latitude(orderRequest.latitude())
                    .longitude(orderRequest.longitude())
                    .address(orderRequest.address())
                    .packageWeight(orderRequest.packageWeight())
                    .priority(orderRequest.priority())
                    .orderStatus(OrderStatus.NOT_ASSIGNED)
                    .build();

            ordersToBeSaved.add(order);
            duplicateCheckingSet.add(orderRequest.orderId());
        }

        orderRepository.bulkUpsert(ordersToBeSaved);

        return new OrderSaveResponse("The Orders saved successfully" , Status.SUCCESS);

    }
}
