package com.ecommerce.ecommerce_platform.controllers.service;

import com.ecommerce.ecommerce_platform.config.JwtUtil;
import com.ecommerce.ecommerce_platform.config.MyUserDetailsService;
import com.ecommerce.ecommerce_platform.controller.CustomerController;
import com.ecommerce.ecommerce_platform.dto.AuthenticationRequestDto;
import com.ecommerce.ecommerce_platform.dto.CustomerDto;
import com.ecommerce.ecommerce_platform.entity.Customer;
import com.ecommerce.ecommerce_platform.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtTokenUtil;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setPassword("password");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).createCustomer(any(CustomerDto.class));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe Updated");
        customerDto.setEmail("john.doe.updated@example.com");
        customerDto.setPassword("newpassword");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe Updated");
        customer.setEmail("john.doe.updated@example.com");

        when(customerService.updateCustomer(eq(1L), any(CustomerDto.class))).thenReturn(customer);

        mockMvc.perform(put("/api/v1/customers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe Updated\",\"email\":\"john.doe.updated@example.com\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(CustomerDto.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/v1/customers/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void testRegisterCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setPassword("password");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(customerService, times(1)).createCustomer(any(CustomerDto.class));
    }

    @Test
    void testCreateAuthenticationToken() throws Exception {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto();
        authRequest.setUsername("john.doe@example.com");
        authRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("john.doe@example.com")).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/api/v1/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("fake-jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("john.doe@example.com");
        verify(jwtTokenUtil, times(1)).generateToken(userDetails);
    }
}
