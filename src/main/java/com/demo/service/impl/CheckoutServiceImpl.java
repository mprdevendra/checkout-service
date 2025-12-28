package com.demo.service.impl;

import com.demo.dto.BasketDto;
import com.demo.dto.BasketItemDto;
import com.demo.dto.DiscountDetailsDto;
import com.demo.dto.ItemDetailsDto;
import com.demo.entity.Product;
import com.demo.exception.DTOMapperServiceException;
import com.demo.exception.InvalidInputException;
import com.demo.exception.PriceCalculationException;
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
    public BasketPricingResponseDto calculateBasketPrice(BasketDto basketDto) throws InvalidInputException {
        List<BasketItemDto> basketItems = basketDto.getBasketItems();
        List<String> itemNames = basketItems.stream().map(BasketItemDto::getItemName).toList();
        List<Product> products = productServiceImpl.getProductsByNames(itemNames);
        validateBucketItem(itemNames, products);
        List<ItemDetailsDto> itemDetailsDtos = calculateItemPrice(basketItems, products);
        List<DiscountDetailsDto> discountDetailsDtos = promotionServiceImpl.calculateDiscount(itemDetailsDtos);
        return receiptResponseMapper(itemDetailsDtos, discountDetailsDtos);
    }

    private void validateBucketItem(List<String> itemNames, List<Product> products) throws InvalidInputException {
        if (products.isEmpty()) {
            throw new InvalidInputException("Product(s) are not available. Product(s) : " + itemNames);
        }
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getName, product -> product));

        List<String> notMatchedItem = itemNames.stream()
                .filter(itemName -> !productMap.containsKey(itemName))
                .toList();

        if (!notMatchedItem.isEmpty()) {
            throw new InvalidInputException("Product(s) are not available. Product(s) : " + notMatchedItem);
        }
    }

    private List<ItemDetailsDto> calculateItemPrice(List<BasketItemDto> basketItems, List<Product> products) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getName, product -> product));

        return basketItems.stream()
                .map(item -> {
                            Product product = productMap.get(item.getItemName());
                            try{
                                return calculateLineItem(item, product);
                            } catch (Exception ex) {
                                throw new PriceCalculationException("Failed to calculate price for item : "+item.getItemName(),ex);
                            }
                        }).toList();
    }

    private ItemDetailsDto calculateLineItem(BasketItemDto item, Product product) {
        ItemDetailsDto itemDetails = new ItemDetailsDto();
        itemDetails.setName(item.getItemName());
        itemDetails.setQuantity(item.getQuantity());
        itemDetails.setUnitPrice(product.getPrice());
        itemDetails.setLineTotal(lineTotal(item.getQuantity(), product.getPrice()));
        return itemDetails;
    }

    private BigDecimal lineTotal(int quantity, BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


    private BasketPricingResponseDto receiptResponseMapper(List<ItemDetailsDto> itemDetailsDtoList, List<DiscountDetailsDto> discountDetailsDtos) {
        try {
            BasketPricingResponseDto receiptResponse = new BasketPricingResponseDto();
            List<ItemDto> itemVos = itemDetailsDtoList.stream().map(item -> {
                return new ItemDto(item.getName(), item.getQuantity(), item.getLineTotal());
            }).toList();
            receiptResponse.setItems(itemVos);
            List<DiscountDto> discountVos = discountDetailsDtos.stream().map(discount -> new DiscountDto(discount.getDescription(), discount.getAmount())).toList();
            receiptResponse.setDiscounts(discountVos);

            BigDecimal subtotal = calculateSubtotal(itemVos);
            receiptResponse.setSubTotal(subtotal);
            BigDecimal totalDiscount = calculateTotalDiscount(discountVos);
            receiptResponse.setTotalDiscount(totalDiscount);
            BigDecimal total = subtotal.subtract(totalDiscount);
            receiptResponse.setTotal(total);
            return receiptResponse;
        } catch (Exception exception) {
            throw new DTOMapperServiceException("Failed to map basket pricing response."+exception.getMessage());
        }
    }

    private BigDecimal calculateSubtotal(List<ItemDto> itemVos) {

        return itemVos.stream()
                .map(ItemDto::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

    }

    private BigDecimal calculateTotalDiscount(List<DiscountDto> discountVos) {

        return discountVos.stream()
                .map(DiscountDto::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public String formatResponse(BasketPricingResponseDto basketPricingResponseDto){
        //add try catch block exception
        return PrintFormatter.format(basketPricingResponseDto);
    }

}
