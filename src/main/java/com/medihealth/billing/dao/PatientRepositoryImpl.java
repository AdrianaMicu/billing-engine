package com.medihealth.billing.dao;

import com.medihealth.billing.domain.Patient;
import com.medihealth.billing.services.discount.ChildDiscountStrategy;
import com.medihealth.billing.services.discount.SeniorDiscountStrategy;
import com.medihealth.billing.services.discount.YoungSeniorDiscountStrategy;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

    private List<Patient> patientList = new ArrayList<>();

    @PostConstruct
    public void init() {
        Patient patientHealthInsurance = new Patient();
        patientHealthInsurance.setId(1L);
        patientHealthInsurance.setFirstName("John");
        patientHealthInsurance.setLastName("Doe");
        patientHealthInsurance.setAge(30);
        patientHealthInsurance.setInsurance(true);

        Patient patientSeniorNoHealthInsurance = new Patient();
        patientSeniorNoHealthInsurance.setId(2L);
        patientSeniorNoHealthInsurance.setFirstName("Laney");
        patientSeniorNoHealthInsurance.setLastName("Pitcaithley");
        patientSeniorNoHealthInsurance.setAge(80);
        patientSeniorNoHealthInsurance.setDiscountStrategy(new SeniorDiscountStrategy());
        patientSeniorNoHealthInsurance.setInsurance(false);

        Patient patientYoungSeniorHealthInsurance = new Patient();
        patientYoungSeniorHealthInsurance.setId(3L);
        patientYoungSeniorHealthInsurance.setFirstName("Jonis");
        patientYoungSeniorHealthInsurance.setLastName("Danilevich");
        patientYoungSeniorHealthInsurance.setAge(67);
        patientYoungSeniorHealthInsurance.setDiscountStrategy(new YoungSeniorDiscountStrategy());
        patientYoungSeniorHealthInsurance.setInsurance(true);

        Patient patientChildHealthInsurance = new Patient();
        patientChildHealthInsurance.setId(4L);
        patientChildHealthInsurance.setFirstName("Cody");
        patientChildHealthInsurance.setLastName("Schimon");
        patientChildHealthInsurance.setAge(3);
        patientChildHealthInsurance.setDiscountStrategy(new ChildDiscountStrategy());
        patientChildHealthInsurance.setInsurance(true);

        patientList.add(patientHealthInsurance);
        patientList.add(patientSeniorNoHealthInsurance);
        patientList.add(patientYoungSeniorHealthInsurance);
        patientList.add(patientChildHealthInsurance);
    }

    @Override
    public Patient findOneById(Long patientId) {
        return patientList.stream().filter(p -> p.getId().equals(patientId)).findFirst().get();
    }
}
