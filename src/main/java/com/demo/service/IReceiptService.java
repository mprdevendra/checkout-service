package com.demo.service;

import com.demo.api.dto.BasketPricingResponseDto;
import com.demo.api.dto.DiscountDetailsDto;
import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;

import java.util.List;

public interface IReceiptService {

    BasketPricingResponseDto receipt(List<ItemDetailsDto> items, List<DiscountDto> discounts, int basketId);
}
