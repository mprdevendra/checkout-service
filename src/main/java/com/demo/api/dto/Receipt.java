package com.demo.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Receipt {
    private int basketId;
    private List<ItemDto> items;
    private BigDecimal subTotal;
    private List<DiscountDto> discounts;
    private BigDecimal totalDiscount;
    private BigDecimal total;
}
