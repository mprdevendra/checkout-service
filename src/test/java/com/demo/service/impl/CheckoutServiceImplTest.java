package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.PriceCalculationException;
import com.demo.exception.CheckoutServiceException;
import com.demo.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void checkout_onlyRequiredAssertions() {
        BasketItemDto basketItem = new BasketItemDto("Bananas",3);
        BasketDto basketDto = new BasketDto(1, List.of(basketItem));

        BigDecimal subTotal = new BigDecimal("1.50");
        BigDecimal discountTotal = new BigDecimal("0.50");
        BigDecimal total = new BigDecimal("1.00");

        ItemDetailsDto itemDetails = new ItemDetailsDto("Bananas", 3, new BigDecimal("1.50"), new BigDecimal("0.50"));
        ItemDto items = new ItemDto("Bananas", 3, new BigDecimal("1.50"));
        DiscountDto discount = new DiscountDto("Buy 2, Get 1 free(Bananas)", new BigDecimal("0.50"));

        Receipt receipt = new Receipt();
        receipt.setBasketId(1);
        receipt.setItems(List.of(items));
        receipt.setSubTotal(subTotal);
        receipt.setDiscounts(List.of(discount));
        receipt.setTotalDiscount(discountTotal);
        receipt.setTotal(total);

        when(priceServiceImpl.itemPrice(List.of(basketItem)))
                .thenReturn(List.of(itemDetails));
        when(promotionServiceImpl.promotion(List.of(itemDetails)))
                .thenReturn(List.of(discount));
        when(calculationServiceImpl.subTotal(List.of(itemDetails)))
                .thenReturn(subTotal);
        when(calculationServiceImpl.discountTotal(List.of(discount)))
                .thenReturn(discountTotal);
        when(calculationServiceImpl.total(subTotal, discountTotal))
                .thenReturn(total);

        when(receiptServiceImpl.receipt(List.of(itemDetails), List.of(discount), subTotal, discountTotal, total, 1))
                .thenReturn(receipt);

        Receipt result = checkoutService.checkout(basketDto);
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Bananas", result.getItems().get(0).getName());
        assertEquals(3, result.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("1.50"), result.getItems().get(0).getPrice());

        assertEquals(1, result.getDiscounts().size());
        assertEquals("Buy 2, Get 1 free(Bananas)", result.getDiscounts().get(0).getDescription());
        assertEquals(new BigDecimal("0.50"), result.getDiscounts().get(0).getAmount());

        assertEquals(new BigDecimal("1.50"), result.getSubTotal());
        assertEquals(new BigDecimal("0.50"), result.getTotalDiscount());
        assertEquals(new BigDecimal("1.00"), result.getTotal());
    }

    @Test
    void testCheckout_PriceCalculationException_Exception() {
        BasketItemDto item = new BasketItemDto("Unknown", 1);
        BasketDto basketDto = new BasketDto(1, List.of(item));

        when(priceServiceImpl.itemPrice(anyList())).thenThrow(new PriceCalculationException("Exception test message"));

        CheckoutServiceException ex = assertThrows(CheckoutServiceException.class,
                () -> checkoutService.checkout(basketDto));

        assertEquals(500, ex.getErrorCode());
        assertTrue(ex.getMessage().contains("Error occurred"));
    }
}
