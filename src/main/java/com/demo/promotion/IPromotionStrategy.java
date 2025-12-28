package com.demo.promotion;

import com.demo.dto.DiscountDetailsDto;
import com.demo.entity.Promotion;

import java.math.BigDecimal;

public interface IPromotionStrategy {
    DiscountDetailsDto apply(int quantity, BigDecimal unitPrice, Promotion promotion);
}
