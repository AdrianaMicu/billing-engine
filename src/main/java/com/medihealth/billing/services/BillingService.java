package com.medihealth.billing.services;

import com.medihealth.billing.domain.Bill;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;

import java.util.Map;

public interface BillingService {
    Bill createBillForPatient(Long patientId, Map<MedicalServiceType, Integer> serviceTypeAndAmount);
}
