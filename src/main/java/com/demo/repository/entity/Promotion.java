package com.demo.repository.entity;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
@AllArgsConstructor
public final class Promotion {
    private final String productCode;
    private final String promotionType;
    private final Condition condition;
    private final Reward reward;
    private final boolean isActive;

}
