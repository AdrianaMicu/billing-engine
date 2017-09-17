package com.medihealth.billing.domain;

import java.math.BigDecimal;

public class MedicalService {

    public enum MedicalServiceType {
        DIAGNOSIS(0),
        XRAY(0),
        BLOODTEST(15),
        ECG(0),
        VACCINE(0);

        private final int defaultDiscount;

        MedicalServiceType(int defaultDiscount) {
            this.defaultDiscount = defaultDiscount;
        }

        public int getDefaultDiscount() {
            return defaultDiscount;
        }
    }

    private MedicalServiceType type;
    private BigDecimal defaultCost;
    private BigDecimal additionalCharges;

    public MedicalServiceType getType() {
        return type;
    }

    public void setType(MedicalServiceType type) {
        this.type = type;
    }

    public BigDecimal getDefaultCost() {
        return defaultCost;
    }

    public void setDefaultCost(BigDecimal defaultCost) {
        this.defaultCost = defaultCost;
    }

    public BigDecimal getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(BigDecimal additionalCharges) {
        this.additionalCharges = additionalCharges;
    }
}
