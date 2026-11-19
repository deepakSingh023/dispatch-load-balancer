package com.deepaksingh023.dispatch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.deepaksingh023.dispatch.dto.OrderRequest;
import com.deepaksingh023.dispatch.dto.OrdersList;
import com.deepaksingh023.dispatch.entity.Order;
import com.deepaksingh023.dispatch.enums.Priority;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateOrderIdException;
import com.deepaksingh023.dispatch.repository.OrderRepository;
import com.deepaksingh023.dispatch.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void testTheOrderCreationLoopDuplicateLogic(){

        OrderRequest orderRequest = new OrderRequest(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );
        OrderRequest orderRequest2 = new OrderRequest(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );

        List<OrderRequest> orderRequests = new ArrayList<>();
        OrdersList ordersList = new OrdersList(orderRequests);

        orderRequests.add(orderRequest);
        orderRequests.add(orderRequest2);

        assertThrows(DuplicateOrderIdException.class, () -> orderService.saveOrders(ordersList));

        Mockito.verifyNoInteractions(orderRepository);
    }

    @Test
    void testOrderServiceFullUntilSave(){

        OrderRequest orderRequest = new OrderRequest(
                "O1",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );
        OrderRequest orderRequest2 = new OrderRequest(
                "O2",
                20.0,
                50.0,
                "New Delhi",
                20,
                Priority.HIGH
        );

        List<OrderRequest> orderRequests = List.of(orderRequest,orderRequest2);

        OrdersList ordersList = new OrdersList(orderRequests);

        orderService.saveOrders(ordersList);

        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(orderRepository,Mockito.times(1)).bulkUpsert(captor.capture());

        List<Order> orders = captor.getValue();

        assertEquals(2,orders.size());

        assertEquals("O1",orders.get(0).getOrderId());

        assertEquals("O2",orders.get(1).getOrderId());

    }




}
