package com.ecommerce.ecommerce_platform.service;


import com.ecommerce.ecommerce_platform.dto.ProductDto;
import com.ecommerce.ecommerce_platform.entity.Product;
import com.ecommerce.ecommerce_platform.exceptions.ProductNotFoundException;
import com.ecommerce.ecommerce_platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDto) throws ProductNotFoundException {
        return productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            return productRepository.save(product);
        }).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
}
