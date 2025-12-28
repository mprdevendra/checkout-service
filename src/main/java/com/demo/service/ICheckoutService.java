package com.demo.service;

import com.demo.dto.BasketDto;
import com.demo.exception.InvalidInputException;
import com.demo.response.dto.BasketPricingResponseDto;

public interface ICheckoutService {

    BasketPricingResponseDto calculateBasketPrice(BasketDto basketDto) throws InvalidInputException;
    String formatResponse(BasketPricingResponseDto receiptResponse);

}
