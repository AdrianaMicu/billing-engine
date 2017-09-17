package com.medihealth.billing.dao;

import com.medihealth.billing.domain.MedicalService;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;

public interface MedicalServiceRepository {
    MedicalService findOneByType(MedicalServiceType medicalServiceType);
}
