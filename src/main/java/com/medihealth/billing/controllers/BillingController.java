package com.medihealth.billing.controllers;

import com.medihealth.billing.domain.Bill;
import com.medihealth.billing.domain.MedicalService.MedicalServiceType;
import com.medihealth.billing.services.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class BillingController {

    private static final String BILLING_URL = "billing";

    @Autowired
    private BillingService billingService;

    @RequestMapping(value = BILLING_URL, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Bill createBillForPatient(@RequestParam Long patientId, @RequestBody Map<MedicalServiceType, Integer> serviceTypeAndAmount) {
        return billingService.createBillForPatient(patientId, serviceTypeAndAmount);
    }
}
