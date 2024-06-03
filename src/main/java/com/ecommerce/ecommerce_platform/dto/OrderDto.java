package com.ecommerce.ecommerce_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long customerId;
    private List<Long> productIds;
    private double totalAmount;
}
