package com.ecommerce.ecommerce_platform.service;



import com.ecommerce.ecommerce_platform.dto.CustomerDto;
import com.ecommerce.ecommerce_platform.entity.Customer;
import com.ecommerce.ecommerce_platform.exceptions.CustomerNotFoundException;
import com.ecommerce.ecommerce_platform.repository.CustomerRepository;
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

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testCreateCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setPassword("password");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customerDto);

        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("john.doe@example.com", createdCustomer.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Updated Name");
        customerDto.setEmail("updated.email@example.com");
        customerDto.setPassword("newpassword");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = customerService.updateCustomer(1L, customerDto);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Name", updatedCustomer.getName());
        assertEquals("updated.email@example.com", updatedCustomer.getEmail());
        assertEquals("newpassword", updatedCustomer.getPassword());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomerNotFound() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Updated Name");
        customerDto.setEmail("updated.email@example.com");
        customerDto.setPassword("newpassword");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, customerDto));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomerNotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(1L);

        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getName());
        assertEquals("john.doe@example.com", foundCustomer.getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository, times(1)).findById(1L);
    }
}

