package com.demo.service.impl;

import com.demo.api.dto.BasketDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.repository.entity.Product;
import com.demo.service.IPriceService;
import com.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PriceServiceImpl implements IPriceService {

    @Autowired
    private IProductService productServiceImpl;

    @Override
    public List<ItemDetailsDto> itemPrice(BasketDto basket) {

        return basket.getBasketItems().stream()
                .map(item -> {
                        Product product = productServiceImpl.find(item.getItemCode());
                        BigDecimal lineTotal = BigDecimal.valueOf(item.getQuantity()).multiply(product.getPrice());
                        return new ItemDetailsDto(item.getItemCode(), item.getQuantity(), lineTotal, product.getPrice());
                }).toList();
    }
}
