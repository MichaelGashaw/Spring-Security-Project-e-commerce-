package com.ecommerce.ecommerce_platform.service;


import com.ecommerce.ecommerce_platform.dto.CustomerDto;
import com.ecommerce.ecommerce_platform.entity.Customer;
import com.ecommerce.ecommerce_platform.exceptions.CustomerNotFoundException;
import com.ecommerce.ecommerce_platform.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, CustomerDto customerDto) throws CustomerNotFoundException {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDto.getName());
            customer.setEmail(customerDto.getEmail());
            customer.setPassword(customerDto.getPassword());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public Customer getCustomerById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }
}
