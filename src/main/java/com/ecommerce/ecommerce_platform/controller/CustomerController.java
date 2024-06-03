package com.ecommerce.ecommerce_platform.controller;

import com.ecommerce.ecommerce_platform.config.JwtUtil;
import com.ecommerce.ecommerce_platform.config.MyUserDetailsService;
import com.ecommerce.ecommerce_platform.dto.AuthenticationRequestDto;
import com.ecommerce.ecommerce_platform.dto.AuthenticationResponseDto;
import com.ecommerce.ecommerce_platform.dto.CustomerDto;
import com.ecommerce.ecommerce_platform.entity.Customer;
import com.ecommerce.ecommerce_platform.exceptions.CustomerNotFoundException;
import com.ecommerce.ecommerce_platform.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDto customerDto) {
        Customer createdCustomer = customerService.createCustomer(customerDto);
        return ResponseEntity.ok(createdCustomer);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        return ResponseEntity.ok(customerService.createCustomer(customerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
    }
}
