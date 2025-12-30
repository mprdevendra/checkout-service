package com.demo.service;

import com.demo.api.dto.BasketDto;
import com.demo.exception.CheckoutServiceException;
import com.demo.api.dto.Receipt;

public interface ICheckoutService {

    Receipt checkout(BasketDto basketDto) throws CheckoutServiceException;

}
