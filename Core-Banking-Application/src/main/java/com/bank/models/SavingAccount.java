package com.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@DiscriminatorValue("SAVING")
@Data
public class SavingAccount extends Account {

    
    @JsonIgnore
    private double MIN_BALANCE = 500d;

    
    @JsonIgnore
    private double withdrawLimit = 1000000d;
    
    @PrePersist
    public void onCreate() {
    	this.MIN_BALANCE = 500d;
    	this.withdrawLimit = 1000000d;
    }

}
