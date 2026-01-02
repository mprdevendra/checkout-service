package com.demo.service;

import com.demo.api.dto.BasketDto;
import com.demo.api.dto.BasketItemDto;
import com.demo.api.dto.ItemDetailsDto;

import java.util.List;

public interface IPriceService {

    public List<ItemDetailsDto> itemPrice(BasketDto basket);
}
