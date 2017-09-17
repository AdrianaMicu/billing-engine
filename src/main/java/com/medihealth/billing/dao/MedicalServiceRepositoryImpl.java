package com.medihealth.billing.dao;

import com.medihealth.billing.domain.MedicalService;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalServiceRepositoryImpl implements MedicalServiceRepository {

    private List<MedicalService> medicalServiceList = new ArrayList<>();

    @PostConstruct
    public void init() {
        MedicalService diagnosis = new MedicalService();
        diagnosis.setType(MedicalServiceType.DIAGNOSIS);
        diagnosis.setDefaultCost(new BigDecimal("60"));
        diagnosis.setAdditionalCharges(new BigDecimal("0"));

        MedicalService xRay = new MedicalService();
        xRay.setType(MedicalServiceType.XRAY);
        xRay.setDefaultCost(new BigDecimal("150"));
        xRay.setAdditionalCharges(new BigDecimal("0"));

        MedicalService bloodTest = new MedicalService();
        bloodTest.setType(MedicalServiceType.BLOODTEST);
        bloodTest.setDefaultCost(new BigDecimal("78"));
        bloodTest.setAdditionalCharges(new BigDecimal("0"));

        MedicalService ecg = new MedicalService();
        ecg.setType(MedicalServiceType.ECG);
        ecg.setDefaultCost(new BigDecimal("200.4"));
        ecg.setAdditionalCharges(new BigDecimal("0"));

        MedicalService vaccine = new MedicalService();
        vaccine.setType(MedicalServiceType.VACCINE);
        vaccine.setDefaultCost(new BigDecimal("15"));
        vaccine.setAdditionalCharges(new BigDecimal("27.5"));

        medicalServiceList.add(diagnosis);
        medicalServiceList.add(xRay);
        medicalServiceList.add(bloodTest);
        medicalServiceList.add(ecg);
        medicalServiceList.add(vaccine);

    }

    @Override
    public MedicalService findOneByType(MedicalServiceType medicalServiceType) {
        return medicalServiceList.stream().filter(l -> l.getType().equals(medicalServiceType)).findFirst().get();
    }
}
