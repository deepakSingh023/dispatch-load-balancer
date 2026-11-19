package com.deepaksingh023.dispatch.service;

import com.deepaksingh023.dispatch.repository.OrderRepository;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteService {

    private final OrderRepository orderRepository;

    private final VehicleRepository vehicleRepository;

    @Transactional
    public void deleteAllEntries(){

        //Delete directly without selecting
        orderRepository.deleteAllInBatch();
        vehicleRepository.deleteAllInBatch();


    }
}
