package com.demo.service;

import com.demo.api.dto.DiscountDetailsDto;
import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;

import java.util.List;

public interface IPromotionService {

    List<DiscountDto> calculateDiscount(List<ItemDetailsDto> itemDetailsDtos);
}
