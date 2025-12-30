package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.DTOMapperServiceException;
import com.demo.service.IReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class ReceiptServiceImpl implements IReceiptService {

    @Override
    public Receipt receipt(List<ItemDetailsDto> items, List<DiscountDto> discounts, BigDecimal subTotal, BigDecimal discountTotal, BigDecimal total, int basketId) {
        log.debug("Generating the receipt for BasketId={}", basketId);
        try {
            List<ItemDto> itemDtos = items.stream()
                    .map(item ->
                            new ItemDto(item.getName(), item.getQuantity(), item.getLineTotal()))
                    .toList();

            return Receipt.builder()
                    .basketId(basketId)
                    .items(itemDtos)
                    .subTotal(subTotal)
                    .discounts(discounts)
                    .totalDiscount(discountTotal)
                    .total(total)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to map basket pricing response", ex);
            throw new DTOMapperServiceException("Failed to map basket pricing response", ex);
        }
    }
}
