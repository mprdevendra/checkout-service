package com.demo.service;

import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;

import java.math.BigDecimal;
import java.util.List;

public interface ICalculationService {

    BigDecimal subTotal(List<ItemDetailsDto> items);

    BigDecimal discountTotal(List<DiscountDto> discounts);

    BigDecimal total(BigDecimal subTotal,BigDecimal discountTotal);

}
