package com.ecommerce.ecommerce_platform.repository;

import com.ecommerce.ecommerce_platform.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
