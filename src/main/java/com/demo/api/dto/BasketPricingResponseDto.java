package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BasketPricingResponseDto {
    private int basketId;
    private List<ItemDto> items;
    private BigDecimal subTotal;
    private List<DiscountDto> discounts;
    private BigDecimal totalDiscount;
    private BigDecimal total;
}
