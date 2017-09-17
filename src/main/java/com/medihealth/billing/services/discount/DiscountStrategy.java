package com.medihealth.billing.services.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(BigDecimal price);
}
