package com.ecommerce.ecommerce_platform.service;


import com.ecommerce.ecommerce_platform.dto.OrderDto;
import com.ecommerce.ecommerce_platform.entity.Customer;
import com.ecommerce.ecommerce_platform.entity.Order;
import com.ecommerce.ecommerce_platform.entity.Product;
import com.ecommerce.ecommerce_platform.exceptions.CustomerNotFoundException;
import com.ecommerce.ecommerce_platform.exceptions.OrderNotFoundException;
import com.ecommerce.ecommerce_platform.exceptions.ProductNotFoundException;
import com.ecommerce.ecommerce_platform.repository.CustomerRepository;
import com.ecommerce.ecommerce_platform.repository.OrderRepository;
import com.ecommerce.ecommerce_platform.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        Customer customer = new Customer();
        customer.setId(1L);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(200.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(orderDto.getProductIds())).thenReturn(Arrays.asList(product1, product2));

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(Arrays.asList(product1, product2));
        order.setTotalAmount(300.0);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(orderDto);

        assertNotNull(createdOrder);
        assertEquals(300.0, createdOrder.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderCustomerNotFound() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    void testCreateOrderProductNotFound() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(orderDto.getProductIds())).thenReturn(Arrays.asList());

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    void testUpdateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        Customer customer = new Customer();
        customer.setId(1L);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(200.0);

        Order existingOrder = new Order();
        existingOrder.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(orderDto.getProductIds())).thenReturn(Arrays.asList(product1, product2));

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomer(customer);
        updatedOrder.setProducts(Arrays.asList(product1, product2));
        updatedOrder.setTotalAmount(300.0);

        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, orderDto);

        assertNotNull(result);
        assertEquals(300.0, result.getTotalAmount());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrderNotFound() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(1L, orderDto));
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));
        verify(orderRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }
}

