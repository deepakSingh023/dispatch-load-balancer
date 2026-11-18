package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
