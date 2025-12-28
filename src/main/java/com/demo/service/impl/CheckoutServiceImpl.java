package com.demo.service.impl;

import com.demo.dto.BasketDto;
import com.demo.dto.BasketItemDto;
import com.demo.dto.DiscountDetailsDto;
import com.demo.dto.ItemDetailsDto;
import com.demo.entity.Product;
import com.demo.exception.*;
import com.demo.response.formatter.PrintFormatter;
import com.demo.service.ICheckoutService;
import com.demo.service.IProductService;
import com.demo.service.IPromotionService;
import com.demo.response.dto.DiscountDto;
import com.demo.response.dto.ItemDto;
import com.demo.response.dto.BasketPricingResponseDto;
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

    @Override
    public BasketPricingResponseDto calculateBasketPrice(BasketDto basketDto) {
        Integer basketId = basketDto.getBasketId();
        List<BasketItemDto> basketItems = basketDto.getBasketItems();
        List<String> itemNames = basketItems.stream().map(BasketItemDto::getItemName).toList();
        log.info("Starting basket price calculation. BasketId={}", basketId);
        try{
            List<Product> products = productServiceImpl.getProductsByNames(itemNames);
            Map<String, Product> productMap = productMap(products);
            List<ItemDetailsDto> itemDetailsDtos = calculateItemPrice(basketItems, productMap);
            List<DiscountDetailsDto> discountDetailsDtos = promotionServiceImpl.calculateDiscount(itemDetailsDtos);
            BasketPricingResponseDto basketPriceResponse = mapToBasketPricingResponse(itemDetailsDtos, discountDetailsDtos,basketId);
            log.info("Basket price calculation successful. BasketId={}", basketId);
            return basketPriceResponse;
        } catch (ProductNotFoundException ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            int errorCode = 404;
            throw new CheckoutServiceException(errorCode, ex.getMessage()+" : BucketId : "+ basketId,ex);
        } catch (Exception ex) {
            log.error("{} BasketId : {}", ex.getMessage(), basketId, ex);
            int errorCode = 500;
            throw new CheckoutServiceException(errorCode, "Error occurred while calculating the basket price for BasketId : "+ basketId,ex);
        }
    }

    private Map<String, Product> productMap(List<Product> products){
        return products.stream()
                .collect(Collectors.toMap(Product::getName, p -> p));
    }

    private List<ItemDetailsDto> calculateItemPrice(List<BasketItemDto> basketItems, Map<String, Product> productMap) {
        return basketItems.stream()
                .map(item -> {
                            try {
                                return calculateLineItem(item, productMap);
                            } catch (Exception ex) {
                                throw new PriceCalculationException(
                                        "Failed to calculate price for item: " + item.getItemName(), ex
                                );
                            }
                        }
                ).toList();
    }

    private ItemDetailsDto calculateLineItem(BasketItemDto item, Map<String, Product> productMap) {
        Product product = productMap.get(item.getItemName());
        BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new ItemDetailsDto(item.getItemName(), item.getQuantity(), lineTotal,product.getPrice());
    }

    private BasketPricingResponseDto mapToBasketPricingResponse(List<ItemDetailsDto> items, List<DiscountDetailsDto> discounts, int basketId) {
        log.debug("Mapping basket pricing response. BasketId={}", basketId);
        try {
            List<ItemDto> itemDtos = items.stream()
                    .map(item -> new ItemDto(item.getName(), item.getQuantity(), item.getLineTotal()))
                    .toList();

            List<DiscountDto> discountDtos = discounts.stream()
                    .map(discount -> new DiscountDto(discount.getDescription(), discount.getAmount()))
                    .toList();

            BigDecimal subTotal = sumAmounts(itemDtos.stream().map(ItemDto::getPrice).toList());
            BigDecimal totalDiscount = sumAmounts(discountDtos.stream().map(DiscountDto::getAmount).toList());
            BigDecimal total = subTotal.subtract(totalDiscount);
            BasketPricingResponseDto basketPricingResponseDto = new BasketPricingResponseDto(basketId, itemDtos, subTotal, discountDtos, totalDiscount, total);
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

    @Override
    public String formatResponse(BasketPricingResponseDto response){
        log.debug("Starting formatting of basket pricing response for basketId={}", response.getBasketId());
        try{
            String formattedResponse = PrintFormatter.format(response);
            log.debug("Successfully formatted basket pricing response for basketId={}", response.getBasketId());
            return formattedResponse;
        } catch (Exception ex) {
            log.error("Error formatting basket pricing response. BasketId={}",response.getBasketId(), ex);
            throw new ResponseFormatterException("Error while formatting response for basketId : "+response.getBasketId(),ex);
        }
    }

}
