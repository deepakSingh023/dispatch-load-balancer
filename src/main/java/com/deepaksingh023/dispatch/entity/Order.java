package com.deepaksingh023.dispatch.entity;


import com.deepaksingh023.dispatch.enums.OrderStatus;
import com.deepaksingh023.dispatch.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "orders_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {


    @Id
    @Column(nullable = false , length = 50)
    private String orderId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false , length = 500)
    private String address;

    @Column(nullable = false)
    private Integer packageWeight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
