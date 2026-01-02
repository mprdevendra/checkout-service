package com.demo.service;

import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.repository.entity.Promotion;

import java.math.BigDecimal;
import java.util.Optional;

public interface IPromotionStrategy {
    Optional<DiscountDto> apply(ItemDetailsDto item, Promotion promotion);
}
