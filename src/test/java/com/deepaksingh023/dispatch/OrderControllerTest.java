package com.deepaksingh023.dispatch;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.deepaksingh023.dispatch.controller.OrderController;
import com.deepaksingh023.dispatch.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    OrderService orderService;



    @Test
    void shouldReturn200ForAValisData() throws Exception{

        String input = """
            {
                "orders": [
                 {
                    "orderId": "ORD001",
                    "latitude": 12.9716,
                    "longitude": 77.5946,
                    "address": "MG Road, Bangalore, Karnataka, India",
                    "packageWeight": 10,
                    "priority": "HIGH"
                 },
                 {
                     "orderId": "ORD002",
                     "latitude": 13.0827,
                     "longitude": 80.2707,
                     "address": "Anna Salai, Chennai, Tamil Nadu, India",
                     "packageWeight": 20,
                     "priority": "MEDIUM"
                 }
               ]
            }
        """;


        mockMvc.perform(post("/api/dispatch/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andExpect(status().isOk());

        Mockito.verify(orderService,Mockito.times(1)).saveOrders(any());

    }

    @Test
    void shouldThrowExceptionOnMalformedDataTest() throws Exception{

        String input = """
            {
                "orders": [
                 {
                    "orderId": "ORD001",
                    "latitude": 12.9716,
                    "longitude": 77.5946,
                    "priority": "HIGH"
                 },
                 {
                     "orderId": "ORD002",
                     "latitude": 13.0827,
                     "address": "Anna Salai, Chennai, Tamil Nadu, India",
                     "packageWeight": 20,

                 }
               ]
            }
        """;

        mockMvc.perform(post("/api/dispatch/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(orderService);


    }




}
