package com.medihealth.billing.dao;

import com.medihealth.billing.domain.Bill;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BillRepositoryImpl implements BillReopsitory {

    List<Bill> billList = new ArrayList<>();

    @Override
    public Bill save(Bill bill) {
        bill.setId(UUID.randomUUID());
        billList.add(bill);
        return bill;
    }
}
