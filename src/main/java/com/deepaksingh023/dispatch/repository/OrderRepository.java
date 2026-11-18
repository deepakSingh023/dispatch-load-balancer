package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String>,OrderRepositoryInterface {

    @Query("SELECT o FROM Order o WHERE o.orderStatus = com.deepaksingh023.dispatch.enums.OrderStatus.UN_ASSIGNED")
    List<Order> getUnassignedOrders();

}
