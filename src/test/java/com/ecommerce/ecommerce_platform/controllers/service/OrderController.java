package com.ecommerce.ecommerce_platform.controllers.service;

import com.ecommerce.ecommerce_platform.controller.OrderController;
import com.ecommerce.ecommerce_platform.dto.OrderDto;
import com.ecommerce.ecommerce_platform.entity.Order;
import com.ecommerce.ecommerce_platform.exceptions.OrderNotFoundException;
import com.ecommerce.ecommerce_platform.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));
        orderDto.setTotalAmount(300.0);

        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(300.0);

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"productIds\":[1,2],\"totalAmount\":300.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalAmount").value(300.0));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testUpdateOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));
        orderDto.setTotalAmount(300.0);

        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(300.0);

        when(orderService.updateOrder(eq(1L), any(OrderDto.class))).thenReturn(order);

        mockMvc.perform(put("/api/v1/orders/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"productIds\":[1,2],\"totalAmount\":300.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalAmount").value(300.0));

        verify(orderService, times(1)).updateOrder(eq(1L), any(OrderDto.class));
    }

    @Test
    void testUpdateOrderNotFound() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));
        orderDto.setTotalAmount(300.0);

        when(orderService.updateOrder(eq(1L), any(OrderDto.class))).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(put("/api/v1/orders/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"productIds\":[1,2],\"totalAmount\":300.0}"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).updateOrder(eq(1L), any(OrderDto.class));
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/v1/orders/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void testDeleteOrderNotFound() throws Exception {
        doThrow(new OrderNotFoundException("Order not found")).when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/v1/orders/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(300.0);

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalAmount").value(300.0));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getOrderById(1L);
    }
}
