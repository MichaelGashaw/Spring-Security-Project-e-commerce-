package com.ecommerce.ecommerce_platform.entity;

import com.ecommerce.ecommerce_platform.repository.CustomerRepository;
import com.ecommerce.ecommerce_platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

;

@Component
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Sample products
        productRepository.save(new Product(null, "Product1", "Description1", 100.0, 10));
        productRepository.save(new Product(null, "Product2", "Description2", 200.0, 20));

        // Sample customers
        customerRepository.save(new Customer(null, "Customer1", "customer1@example.com", "password"));
        customerRepository.save(new Customer(null, "Customer2", "customer2@example.com", "password"));




    }

}

