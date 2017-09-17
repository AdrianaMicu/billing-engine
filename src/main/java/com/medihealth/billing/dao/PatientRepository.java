package com.medihealth.billing.dao;

import com.medihealth.billing.domain.Patient;

public interface PatientRepository {
    Patient findOneById(Long patientId);
}
