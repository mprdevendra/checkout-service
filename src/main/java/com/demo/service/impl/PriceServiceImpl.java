package com.demo.service.impl;

import com.demo.api.dto.BasketItemDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.exception.PriceCalculationException;
import com.demo.repository.entity.Product;
import com.demo.service.IPriceService;
import com.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PriceServiceImpl implements IPriceService {

    @Autowired
    private IProductService productServiceImpl;

    @Override
    public List<ItemDetailsDto> itemPrice(List<BasketItemDto> basketItems){
        List<String> itemNames = basketItems.stream().map(BasketItemDto::getItemName).toList();
        List<Product> products = productServiceImpl.getProductsByNames(itemNames);

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
