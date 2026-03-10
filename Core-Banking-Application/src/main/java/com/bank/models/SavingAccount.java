package com.bank.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue("SAVING")
public class SavingAccount extends Account {

    
  @PrePersist
    public void generateAccountNumber() {

        if (getAccNo() == null) {

            String date = LocalDate.now()
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            long sequence = (long) (Math.random() * 9000) + 1000;

            long accNo = Long.parseLong(date + "1" + sequence);

            setAccNo(accNo);
        }
    }

}
