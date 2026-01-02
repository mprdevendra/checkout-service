package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.CheckoutServiceException;
import com.demo.exception.ProductNotFoundException;
import com.demo.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Mock
    private IPriceService priceServiceImpl;

    @Mock
    private IPromotionService promotionServiceImpl;

    @Mock
    private IReceiptService receiptServiceImpl;

    @Mock
    private ICalculationService calculationServiceImpl;

    @Test
    void test_Checkout_Success() {
        BasketDto basket = new BasketDto(1, List.of());
        List<ItemDetailsDto> items = List.of(new ItemDetailsDto("BANANAS", 3, bd("0.50"), bd("1.50")));
        List<DiscountDto> discounts = List.of(new DiscountDto("Buy 2, Get 1 free(Bananas)", bd("0.50")));
        BigDecimal subTotal = bd("1.50");
        BigDecimal discountTotal = bd("0.50");
        BigDecimal total = bd("1.00");
        ItemDto itemDto = new ItemDto("BANANAS",3,bd("1.50"));
        Receipt receipt = new Receipt(1, List.of(itemDto), subTotal, discounts, discountTotal, total);

        when(priceServiceImpl.itemPrice(basket)).thenReturn(items);
        when(promotionServiceImpl.promotion(items)).thenReturn(discounts);
        when(calculationServiceImpl.subTotal(items)).thenReturn(subTotal);
        when(calculationServiceImpl.discountTotal(discounts)).thenReturn(discountTotal);
        when(calculationServiceImpl.total(subTotal, discountTotal)).thenReturn(total);
        when(receiptServiceImpl.receipt(items, discounts, subTotal, discountTotal, total, basket.getBasketId()))
                .thenReturn(receipt);

        Receipt result = checkoutService.checkout(basket);

        // Assert
        assertNotNull(result);

        //Items Assert
        assertEquals("BANANAS", result.getItems().get(0).getItemCode());
        assertEquals(3, result.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("1.50"), result.getItems().get(0).getPrice());

        //Discount Assert
        assertEquals(1, result.getDiscounts().size());
        assertEquals("Buy 2, Get 1 free(Bananas)", result.getDiscounts().get(0).getDescription());
        assertEquals(new BigDecimal("0.50"), result.getDiscounts().get(0).getAmount());

        assertEquals(subTotal, result.getSubTotal());
        assertEquals(discountTotal,result.getTotalDiscount());
        assertEquals(total, result.getTotal());

        verify(priceServiceImpl, times(1)).itemPrice(basket);
        verify(promotionServiceImpl, times(1)).promotion(items);
        verify(calculationServiceImpl, times(1)).subTotal(items);
        verify(calculationServiceImpl, times(1)).discountTotal(discounts);
        verify(calculationServiceImpl, times(1)).total(subTotal, discountTotal);
        verify(receiptServiceImpl, times(1))
                .receipt(items, discounts, subTotal, discountTotal, total, basket.getBasketId());
    }

    @Test
    void test_checkout_ProductNotFoundException() {
        BasketDto basket = new BasketDto(1, List.of());
        when(priceServiceImpl.itemPrice(any(BasketDto.class))).thenThrow(new ProductNotFoundException("Test Exception message"));

        CheckoutServiceException exception = assertThrows(CheckoutServiceException.class,
                () -> checkoutService.checkout(basket));

        assertEquals(404, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Test Exception message"));
    }

    private BigDecimal bd(String value){
        return new BigDecimal(value);
    }
}
