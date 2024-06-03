package com.ecommerce.ecommerce_platform.repository;

import com.ecommerce.ecommerce_platform.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
