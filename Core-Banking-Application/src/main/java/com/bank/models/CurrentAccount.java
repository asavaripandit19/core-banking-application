package com.bank.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends Account{

	private Double MIN_BALANCE = 10000d;
	
	  @PrePersist
	    public void onCreate() {
	    	this.MIN_BALANCE = 10000d;
	    	
	    }
	
	
}
