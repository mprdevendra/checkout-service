package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.ReceiptServiceException;
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
        try {
            return Receipt.builder()
                    .basketId(basketId)
                    .items(mapToItemDtos(items))
                    .subTotal(subTotal)
                    .discounts(discounts)
                    .totalDiscount(discountTotal)
                    .total(total)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to generating the receipt : ", ex);
            throw new ReceiptServiceException("Failed to generate receipt", ex);
        }
    }

    private List<ItemDto> mapToItemDtos(List<ItemDetailsDto> items) {
        return items.stream()
                .map(item ->
                        new ItemDto(item.getItemCode(), item.getQuantity(), item.getLineTotal()))
                .toList();
    }
}
