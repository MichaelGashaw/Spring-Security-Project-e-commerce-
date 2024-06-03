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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));
        order.setCustomer(customer);

        List<Product> products = (List<Product>) productRepository.findAllById(orderDto.getProductIds());
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with the provided IDs");
        }
        order.setProducts(products);

        double totalAmount = products.stream().mapToDouble(Product::getPrice).sum();
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderDto orderDto) throws OrderNotFoundException {
        return orderRepository.findById(id).map(order -> {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));
            order.setCustomer(customer);

            List<Product> products = (List<Product>) productRepository.findAllById(orderDto.getProductIds());
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No products found with the provided IDs");
            }
            order.setProducts(products);

            double totalAmount = products.stream().mapToDouble(Product::getPrice).sum();
            order.setTotalAmount(totalAmount);

            return orderRepository.save(order);
        }).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}
