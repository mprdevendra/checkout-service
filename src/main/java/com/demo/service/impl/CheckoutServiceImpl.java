package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.*;
import com.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class CheckoutServiceImpl implements ICheckoutService {

    @Autowired
    private IPriceService priceServiceImpl;

    @Autowired
    private IPromotionService promotionServiceImpl;

    @Autowired
    private IReceiptService receiptServiceImpl;

    @Autowired
    private ICalculationService calculationServiceImpl;

    @Override
    public Receipt checkout(BasketDto basketDto) {
        Integer basketId = basketDto.getBasketId();
        List<BasketItemDto> basketItems = basketDto.getBasketItems();
        log.info("Starting checkout for BasketId={}", basketId);
        try{
            List<ItemDetailsDto> items = priceServiceImpl.itemPrice(basketItems);
            List<DiscountDto> discounts = promotionServiceImpl.promotion(items);
            BigDecimal subTotal = calculationServiceImpl.subTotal(items);
            BigDecimal discountTotal = calculationServiceImpl.discountTotal(discounts);
            BigDecimal total = calculationServiceImpl.total(subTotal, discountTotal);
            Receipt receipt = receiptServiceImpl.receipt(items, discounts, subTotal, discountTotal, total, basketId);
            log.info("Basket checkout successfully completed for BasketId={}", basketId);
            return receipt;
        } catch (ProductNotFoundException ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            throw new CheckoutServiceException(404, ex.getMessage()+" : BucketId : "+ basketId,ex);
        } catch (Exception ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            throw new CheckoutServiceException(500, "Error occurred while checkout for BasketId : "+ basketId,ex);
        }
    }
}
