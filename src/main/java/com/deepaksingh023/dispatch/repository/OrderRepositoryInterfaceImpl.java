package com.deepaksingh023.dispatch.repository;

import com.deepaksingh023.dispatch.entity.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class OrderRepositoryInterfaceImpl implements OrderRepositoryInterface{


    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryInterfaceImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void bulkUpsert(List<Order> orders){

        String query = """
                INSERT INTO orders_table(order_id,latitude,longitude,address , package_weight , priority)
                VALUES(?, ?, ?, ?, ?, ?)
                ON CONFLICT(order_id)
                DO UPDATE SET
                   latitude = EXCLUDED.latitude,
                   longitude = EXCLUDED.longitude,
                   address = EXCLUDED.address,
                   package_weight = EXCLUDED.package_weight,
                   priority = EXCLUDED.priority
                """;

        jdbcTemplate.batchUpdate(query,orders,500,(ps, order) ->{

            ps.setString(1,order.getOrderId());
            ps.setObject(2,order.getLatitude());
            ps.setObject(3,order.getLongitude());
            ps.setString(4,order.getAddress());
            ps.setObject(5,order.getPackageWeight());
            ps.setString(6,order.getPriority().name());

        });




    }
}
