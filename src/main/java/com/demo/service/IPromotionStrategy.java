package com.demo.service;

import com.demo.api.dto.DiscountDetailsDto;
import com.demo.api.dto.DiscountDto;
import com.demo.entity.Promotion;

import java.math.BigDecimal;
import java.util.Optional;

public interface IPromotionStrategy {
    Optional<DiscountDto> apply(int quantity, BigDecimal unitPrice, Promotion promotion);
}
