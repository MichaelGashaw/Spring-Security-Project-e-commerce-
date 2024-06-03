package com.ecommerce.ecommerce_platform.controllers.service;


import com.ecommerce.ecommerce_platform.controller.ProductController;
import com.ecommerce.ecommerce_platform.dto.ProductDto;
import com.ecommerce.ecommerce_platform.entity.Product;
import com.ecommerce.ecommerce_platform.exceptions.ProductNotFoundException;
import com.ecommerce.ecommerce_platform.service.ProductService;
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

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("New Product");
        productDto.setDescription("Description of new product");
        productDto.setPrice(100.0);
        productDto.setStock(50);

        Product product = new Product();
        product.setId(1L);
        product.setName("New Product");
        product.setDescription("Description of new product");
        product.setPrice(100.0);
        product.setStock(50);

        when(productService.createProduct(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Product\",\"description\":\"Description of new product\",\"price\":100.0,\"stock\":50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.description").value("Description of new product"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.stock").value(50));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated description");
        productDto.setPrice(200.0);
        productDto.setStock(30);

        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");
        product.setDescription("Updated description");
        product.setPrice(200.0);
        product.setStock(30);

        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(put("/api/v1/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated description\",\"price\":200.0,\"stock\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(200.0))
                .andExpect(jsonPath("$.stock").value(30));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated description");
        productDto.setPrice(200.0);
        productDto.setStock(30);

        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated description\",\"price\":200.0,\"stock\":30}"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Existing Product");
        product.setDescription("Description of existing product");
        product.setPrice(150.0);
        product.setStock(40);

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Existing Product"))
                .andExpect(jsonPath("$.description").value("Description of existing product"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.stock").value(40));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
    }
}
