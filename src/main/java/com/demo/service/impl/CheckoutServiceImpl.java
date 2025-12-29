package com.demo.service.impl;

import com.demo.api.dto.BasketDto;
import com.demo.api.dto.BasketItemDto;
import com.demo.api.dto.DiscountDetailsDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.entity.Product;
import com.demo.exception.*;
import com.demo.service.ICheckoutService;
import com.demo.service.IProductService;
import com.demo.service.IPromotionService;
import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDto;
import com.demo.api.dto.BasketPricingResponseDto;
import com.demo.service.IReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CheckoutServiceImpl implements ICheckoutService {

    @Autowired
    private IProductService productServiceImpl;

    @Autowired
    private IPromotionService promotionServiceImpl;

    @Autowired
    private IReceiptService receiptServiceImpl;

    @Override
    public BasketPricingResponseDto checkout(BasketDto basketDto) {
        Integer basketId = basketDto.getBasketId();
        List<BasketItemDto> basketItems = basketDto.getBasketItems();
        List<String> itemNames = basketItems.stream().map(BasketItemDto::getItemName).toList();
        log.info("Starting basket price calculation. BasketId={}", basketId);
        try{
            List<Product> products = productServiceImpl.getProductsByNames(itemNames);
            List<ItemDetailsDto> itemDetailsDtos = calculateItemPrice(basketItems, products);
            List<DiscountDto> discountDtos = promotionServiceImpl.calculateDiscount(itemDetailsDtos);
            BasketPricingResponseDto response = receiptServiceImpl.receipt(itemDetailsDtos, discountDtos,basketId);
            log.info("Basket price calculation successful. BasketId={}", basketId);
            return response;
        } catch (ProductNotFoundException ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            throw new CheckoutServiceException(404, ex.getMessage()+" : BucketId : "+ basketId,ex);
        } catch (Exception ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            throw new CheckoutServiceException(500, "Error occurred while calculating the basket price for BasketId : "+ basketId,ex);
        }
    }

    private List<ItemDetailsDto> calculateItemPrice(List<BasketItemDto> basketItems, List<Product> products) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getName, p -> p));

        return basketItems.stream()
                .map(item -> {
                            try {
                                Product product = productMap.get(item.getItemName());
                                BigDecimal lineTotal = BigDecimal.valueOf(item.getQuantity()).multiply(product.getPrice());
                                return new ItemDetailsDto(item.getItemName(), item.getQuantity(), lineTotal,product.getPrice());
                            } catch (Exception ex) {
                                throw new PriceCalculationException(
                                        "Failed to calculate price for item: " + item.getItemName(), ex
                                );
                            }
                        }
                ).toList();
    }


}
