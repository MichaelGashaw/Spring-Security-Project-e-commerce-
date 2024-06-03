package com.ecommerce.ecommerce_platform.repository;

import com.ecommerce.ecommerce_platform.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByEmail(String email);
}
