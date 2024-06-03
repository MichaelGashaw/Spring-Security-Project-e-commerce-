package com.ecommerce.ecommerce_platform.service;


import com.ecommerce.ecommerce_platform.dto.ProductDto;
import com.ecommerce.ecommerce_platform.entity.Product;
import com.ecommerce.ecommerce_platform.exceptions.ProductNotFoundException;
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

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testCreateProduct() {
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

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(productDto);

        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getName());
        assertEquals("Description of new product", createdProduct.getDescription());
        assertEquals(100.0, createdProduct.getPrice());
        assertEquals(50, createdProduct.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated description");
        productDto.setPrice(200.0);
        productDto.setStock(30);

        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description of product 1");
        product.setPrice(100.0);
        product.setStock(50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, productDto);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(200.0, updatedProduct.getPrice());
        assertEquals(30, updatedProduct.getStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated description");
        productDto.setPrice(200.0);
        productDto.setStock(30);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDto));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Existing Product");
        product.setDescription("Description of existing product");
        product.setPrice(150.0);
        product.setStock(40);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Existing Product", foundProduct.getName());
        assertEquals("Description of existing product", foundProduct.getDescription());
        assertEquals(150.0, foundProduct.getPrice());
        assertEquals(40, foundProduct.getStock());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }
}

