package com.demo.service.impl;

import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.service.ICalculationService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class CalculationServiceImpl implements ICalculationService {

    @Override
    public BigDecimal subTotal(List<ItemDetailsDto> items) {
        return sumAmounts(items.stream().map(ItemDetailsDto::getLineTotal).toList());
    }

    @Override
    public BigDecimal discountTotal(List<DiscountDto> discounts) {
        return sumAmounts(discounts.stream().map(DiscountDto::getAmount).toList());
    }

    @Override
    public BigDecimal total(BigDecimal subTotal, BigDecimal totalDiscount) {
        return subTotal.subtract(totalDiscount);
    }

    private BigDecimal sumAmounts(List<BigDecimal> amounts) {
        return amounts.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
