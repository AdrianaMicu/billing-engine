package com.medihealth.billing.services;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.medihealth.billing.dao.BillReopsitory;
import com.medihealth.billing.dao.PatientRepository;
import com.medihealth.billing.dao.MedicalServiceRepository;
import com.medihealth.billing.domain.Bill;
import com.medihealth.billing.domain.MedicalService;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;
import com.medihealth.billing.domain.Patient;
import com.medihealth.billing.services.discount.ChildDiscountStrategy;
import com.medihealth.billing.services.discount.SeniorDiscountStrategy;
import com.medihealth.billing.services.discount.YoungSeniorDiscountStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class BillingServiceImplTest {

    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalServiceRepository medicalServiceRepository;

    @Mock
    private BillReopsitory billReopsitory;

    @Captor
    private ArgumentCaptor<Bill> billArgumentCaptor;

    private Patient patientHealthInsurance;
    private Patient patientSeniorNoHealthInsurance;
    private Patient patientYoungSeniorHealthInsurance;
    private Patient patientChildHealthInsurance;
    private Patient patientYoungSeniorNoHealthInsurance;
    private Patient patientNoHealthInsurance;

    private MedicalService diagnosis;
    private MedicalService xRay;
    private MedicalService bloodTest;
    private MedicalService ecg;
    private MedicalService vaccine;

    @Before
    public void setup() {
        patientHealthInsurance = new Patient();
        patientHealthInsurance.setId(1L);
        patientHealthInsurance.setFirstName("John");
        patientHealthInsurance.setLastName("Doe");
        patientHealthInsurance.setAge(30);
        patientHealthInsurance.setInsurance(true);

        patientSeniorNoHealthInsurance = new Patient();
        patientSeniorNoHealthInsurance.setId(2L);
        patientSeniorNoHealthInsurance.setFirstName("Laney");
        patientSeniorNoHealthInsurance.setLastName("Pitcaithley");
        patientSeniorNoHealthInsurance.setAge(80);
        patientSeniorNoHealthInsurance.setDiscountStrategy(new SeniorDiscountStrategy());
        patientSeniorNoHealthInsurance.setInsurance(false);

        patientYoungSeniorHealthInsurance = new Patient();
        patientYoungSeniorHealthInsurance.setId(3L);
        patientYoungSeniorHealthInsurance.setFirstName("Jonis");
        patientYoungSeniorHealthInsurance.setLastName("Danilevich");
        patientYoungSeniorHealthInsurance.setAge(67);
        patientYoungSeniorHealthInsurance.setDiscountStrategy(new YoungSeniorDiscountStrategy());
        patientYoungSeniorHealthInsurance.setInsurance(true);

        patientChildHealthInsurance = new Patient();
        patientChildHealthInsurance.setId(4L);
        patientChildHealthInsurance.setFirstName("Cody");
        patientChildHealthInsurance.setLastName("Schimon");
        patientChildHealthInsurance.setAge(3);
        patientChildHealthInsurance.setDiscountStrategy(new ChildDiscountStrategy());
        patientChildHealthInsurance.setInsurance(true);

        patientYoungSeniorNoHealthInsurance = new Patient();
        patientYoungSeniorNoHealthInsurance.setId(5L);
        patientYoungSeniorNoHealthInsurance.setFirstName("Marge");
        patientYoungSeniorNoHealthInsurance.setLastName("Danilowicz");
        patientYoungSeniorNoHealthInsurance.setAge(68);
        patientYoungSeniorNoHealthInsurance.setDiscountStrategy(new YoungSeniorDiscountStrategy());
        patientYoungSeniorNoHealthInsurance.setInsurance(false);

        patientNoHealthInsurance = new Patient();
        patientNoHealthInsurance.setId(6L);
        patientNoHealthInsurance.setFirstName("Sadye");
        patientNoHealthInsurance.setLastName("Henker");
        patientNoHealthInsurance.setAge(35);
        patientNoHealthInsurance.setInsurance(false);

        diagnosis = new MedicalService();
        diagnosis.setType(MedicalService.MedicalServiceType.DIAGNOSIS);
        diagnosis.setDefaultCost(new BigDecimal("60"));
        diagnosis.setAdditionalCharges(new BigDecimal("0"));

        xRay = new MedicalService();
        xRay.setType(MedicalService.MedicalServiceType.XRAY);
        xRay.setDefaultCost(new BigDecimal("150"));
        xRay.setAdditionalCharges(new BigDecimal("0"));

        bloodTest = new MedicalService();
        bloodTest.setType(MedicalService.MedicalServiceType.BLOODTEST);
        bloodTest.setDefaultCost(new BigDecimal("78"));
        bloodTest.setAdditionalCharges(new BigDecimal("0"));

        ecg = new MedicalService();
        ecg.setType(MedicalService.MedicalServiceType.ECG);
        ecg.setDefaultCost(new BigDecimal("200.4"));
        ecg.setAdditionalCharges(new BigDecimal("0"));

        vaccine = new MedicalService();
        vaccine.setType(MedicalService.MedicalServiceType.VACCINE);
        vaccine.setDefaultCost(new BigDecimal("15"));
        vaccine.setAdditionalCharges(new BigDecimal("27.5"));
    }

    @Test
    public void createBillForPatient_youngSeniorNoHealthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.XRAY, 2);
        serviceTypeAndAmount.put(MedicalServiceType.BLOODTEST, 1);

        when(patientRepository.findOneById(patientYoungSeniorNoHealthInsurance.getId())).thenReturn(patientYoungSeniorNoHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.XRAY)).thenReturn(xRay);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.BLOODTEST)).thenReturn(bloodTest);

        billingService.createBillForPatient(patientYoungSeniorNoHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("175.20"), resultBill.getAmount());
        assertEquals(patientYoungSeniorNoHealthInsurance.getId(), resultBill.getPatientId());
    }

    @Test
    public void createBillForPatient_youngSeniorHealthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.XRAY, 2);
        serviceTypeAndAmount.put(MedicalServiceType.BLOODTEST, 1);

        when(patientRepository.findOneById(patientYoungSeniorHealthInsurance.getId())).thenReturn(patientYoungSeniorHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.XRAY)).thenReturn(xRay);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.BLOODTEST)).thenReturn(bloodTest);

        billingService.createBillForPatient(patientYoungSeniorHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("170.52"), resultBill.getAmount());
        assertEquals(patientYoungSeniorHealthInsurance.getId(), resultBill.getPatientId());
    }

    @Test
    public void createBillForPatient_childHealthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.BLOODTEST, 1);
        serviceTypeAndAmount.put(MedicalServiceType.VACCINE, 2);

        when(patientRepository.findOneById(patientChildHealthInsurance.getId())).thenReturn(patientChildHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.BLOODTEST)).thenReturn(bloodTest);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.VACCINE)).thenReturn(vaccine);

        billingService.createBillForPatient(patientChildHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("110.28"), resultBill.getAmount());
        assertEquals(patientChildHealthInsurance.getId(), resultBill.getPatientId());
    }

    @Test
    public void createBillForPatient_seniorNoHealthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.XRAY, 1);
        serviceTypeAndAmount.put(MedicalServiceType.ECG, 2);

        when(patientRepository.findOneById(patientSeniorNoHealthInsurance.getId())).thenReturn(patientSeniorNoHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.XRAY)).thenReturn(xRay);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.ECG)).thenReturn(ecg);

        billingService.createBillForPatient(patientSeniorNoHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("61.08"), resultBill.getAmount());
        assertEquals(patientSeniorNoHealthInsurance.getId(), resultBill.getPatientId());
    }

    @Test
    public void createBillForPatient_healthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.BLOODTEST, 2);
        serviceTypeAndAmount.put(MedicalServiceType.VACCINE, 2);

        when(patientRepository.findOneById(patientHealthInsurance.getId())).thenReturn(patientHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.BLOODTEST)).thenReturn(bloodTest);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.VACCINE)).thenReturn(vaccine);

        billingService.createBillForPatient(patientHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("250.10"), resultBill.getAmount());
        assertEquals(patientHealthInsurance.getId(), resultBill.getPatientId());
    }

    @Test
    public void createBillForPatient_noHealthInsurance() {
        Map<MedicalService.MedicalServiceType, Integer> serviceTypeAndAmount = new HashMap<>();
        serviceTypeAndAmount.put(MedicalServiceType.DIAGNOSIS, 1);
        serviceTypeAndAmount.put(MedicalServiceType.BLOODTEST, 2);
        serviceTypeAndAmount.put(MedicalServiceType.VACCINE, 2);

        when(patientRepository.findOneById(patientNoHealthInsurance.getId())).thenReturn(patientNoHealthInsurance);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.DIAGNOSIS)).thenReturn(diagnosis);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.BLOODTEST)).thenReturn(bloodTest);
        when(medicalServiceRepository.findOneByType(MedicalServiceType.VACCINE)).thenReturn(vaccine);

        billingService.createBillForPatient(patientNoHealthInsurance.getId(), serviceTypeAndAmount);

        verify(billReopsitory).save(billArgumentCaptor.capture());

        Bill resultBill = billArgumentCaptor.getValue();
        assertEquals(new BigDecimal("273.50"), resultBill.getAmount());
        assertEquals(patientNoHealthInsurance.getId(), resultBill.getPatientId());
    }
}
