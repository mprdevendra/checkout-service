package com.demo.service;

import com.demo.api.dto.BasketDto;
import com.demo.exception.CheckoutServiceException;
import com.demo.exception.InvalidInputException;
import com.demo.api.dto.BasketPricingResponseDto;

public interface ICheckoutService {

    BasketPricingResponseDto checkout(BasketDto basketDto) throws InvalidInputException, CheckoutServiceException;

}
