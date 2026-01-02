package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailsDto {
    private String itemCode;
    private int quantity;
    private BigDecimal lineTotal;
    private BigDecimal unitPrice;
}
