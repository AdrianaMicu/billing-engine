package com.medihealth.billing.services;

import com.medihealth.billing.dao.BillReopsitory;
import com.medihealth.billing.dao.PatientRepository;
import com.medihealth.billing.dao.MedicalServiceRepository;
import com.medihealth.billing.domain.Bill;
import com.medihealth.billing.domain.Patient;
import com.medihealth.billing.domain.MedicalService;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;
import com.medihealth.billing.services.discount.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalServiceRepository medicalServiceRepository;

    @Autowired
    private BillReopsitory billReopsitory;

    @Override
    public Bill createBillForPatient(Long patientId, Map<MedicalServiceType, Integer> serviceTypeAndAmount) {
        Patient patient = patientRepository.findOneById(patientId);

        BigDecimal price = new BigDecimal("0");

        for (Map.Entry<MedicalServiceType, Integer> entry : serviceTypeAndAmount.entrySet()) {
            MedicalService medicalService = medicalServiceRepository.findOneByType(entry.getKey());
            price = price.add(getPrice(patient, medicalService).multiply(new BigDecimal(entry.getValue())));
            price = price.add(medicalService.getAdditionalCharges());
        }

        DiscountStrategy discountStrategy = patient.getDiscountStrategy();
        if (discountStrategy != null) {
            price = discountStrategy.calculate(price);
        }

        price = price.setScale(2);

        Bill bill = new Bill();
        bill.setPatientId(patientId);
        bill.setAmount(price);

        return billReopsitory.save(bill);
    }

    private BigDecimal getPrice(Patient patient, MedicalService medicalService) {
        BigDecimal price = medicalService.getDefaultCost();

        int defaultDiscount = medicalService.getType().getDefaultDiscount();
        if (patient.getInsurance() && defaultDiscount > 0) {
            price = price.subtract(price.multiply(new BigDecimal(defaultDiscount).divide(new BigDecimal("100"))));
        }

        return price;
    }
}
