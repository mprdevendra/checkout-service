package com.demo.service;

import com.demo.dto.DiscountDetailsDto;
import com.demo.dto.ItemDetailsDto;
import com.demo.entity.Promotion;

import java.util.List;

public interface IPromotionService {

    List<DiscountDetailsDto> calculateDiscount(List<ItemDetailsDto> itemDetailsDtos);
}
