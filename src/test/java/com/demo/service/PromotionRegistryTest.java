package com.demo.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@ExtendWith(MockitoExtension.class)
public class PromotionRegistryTest {

    @InjectMocks
    private PromotionRegistry promotionRegistry;

    @Mock
    @Qualifier("buyXGetYPromotion")
    private IPromotionStrategy buyXGetYPromotion;

    @Mock
    @Qualifier("fixedBundlePricePromotion")
    private IPromotionStrategy fixedBundlePricePromotion;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = PromotionRegistry.class
                .getDeclaredMethod("registerPromotions");
        method.setAccessible(true);
        method.invoke(promotionRegistry);
    }

    @Test
    public void test_getPromotion_BuyXGetY_success(){
        IPromotionStrategy promotion = promotionRegistry.getPromotion("BUY_X_GET_Y");
        Assertions.assertNotNull(promotion);
        Assertions.assertSame(buyXGetYPromotion, promotion);
    }

    @Test
    public void test_getPromotion_FixedBundlePrice_success(){
        IPromotionStrategy promotion = promotionRegistry.getPromotion("FIXED_BUNDLE_PRICE");
        Assertions.assertNotNull(promotion);
        Assertions.assertSame(fixedBundlePricePromotion, promotion);
    }

    @Test
    public void test_getPromotion_Unknown_Failure(){
        IPromotionStrategy promotion = promotionRegistry.getPromotion("UNKNOWN");
        Assertions.assertNull(promotion);
    }
}
