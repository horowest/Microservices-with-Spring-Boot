package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemTO {
    private Long id;
    private String skuCode;
    private Double price;
    private Integer quantity;
}
