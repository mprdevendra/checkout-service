package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.DTOMapperServiceException;
import com.demo.exception.PrintPriceServiceException;
import com.demo.service.IReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ReceiptServiceImpl implements IReceiptService {

    @Override
    public BasketPricingResponseDto receipt(List<ItemDetailsDto> items, List<DiscountDto> discounts, int basketId) {
        log.debug("Mapping basket pricing response. BasketId={}", basketId);
        try {
            List<ItemDto> itemDtos = items.stream()
                    .map(item -> new ItemDto(item.getName(), item.getQuantity(), item.getLineTotal()))
                    .toList();
            BigDecimal subTotal = sumAmounts(itemDtos.stream().map(ItemDto::getPrice).toList());
            BigDecimal totalDiscount = sumAmounts(discounts.stream().map(DiscountDto::getAmount).toList());
            BigDecimal total = subTotal.subtract(totalDiscount);
            BasketPricingResponseDto basketPricingResponseDto = new BasketPricingResponseDto(basketId, itemDtos, subTotal, discounts, totalDiscount, total);
            log.debug("Basket pricing response mapped successfully. BasketId={}", basketId);
            return basketPricingResponseDto;
        } catch (Exception ex) {
            log.error("Failed to map basket pricing response", ex);
            throw new DTOMapperServiceException("Failed to map basket pricing response",ex);
        }
    }

    private BigDecimal sumAmounts(List<BigDecimal> amounts) {
        return amounts.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

}
