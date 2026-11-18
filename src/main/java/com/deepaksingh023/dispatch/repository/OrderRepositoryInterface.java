package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Order;

import java.util.List;

public interface OrderRepositoryInterface {

     void bulkUpsert(List<Order> orders);
}
