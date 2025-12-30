package com.demo.service;

import com.demo.api.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface IReceiptService {
    Receipt receipt(List<ItemDetailsDto> items, List<DiscountDto> discounts, BigDecimal subTotal, BigDecimal discountTotal, BigDecimal total, int basketId);
}
