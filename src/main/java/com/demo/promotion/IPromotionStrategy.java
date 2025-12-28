package com.demo.promotion;

import com.demo.dto.DiscountDetailsDto;
import com.demo.entity.Promotion;

import java.math.BigDecimal;
import java.util.Optional;

public interface IPromotionStrategy {
    Optional<DiscountDetailsDto> apply(int quantity, BigDecimal unitPrice, Promotion promotion);
}
