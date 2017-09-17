package com.medihealth.billing.services.discount;

import java.math.BigDecimal;

public class ChildDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(BigDecimal price) {
        return price.multiply(new BigDecimal("0.6"));
    }
}
