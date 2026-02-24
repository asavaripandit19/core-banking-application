package com.bank.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Pay_Spring_Bank")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Acctype", discriminatorType = DiscriminatorType.STRING)
public class Account {

	@Id
	private Long accNo = 0l;

	@NotBlank
	private String name;

	@Column(unique = true)
	@Email(message = "Invalid Email formate ")
	private String email;

	@Size(min = 10, max = 10)
	@NotNull
	private String mob;

	@NotNull(message = "Aadhar Numbers is requried")
	@Pattern(regexp = "^[0-9]{12}$", message = "Aadhar number must be exactly 12 digits")
	@Column(unique = true)
	private String aadharNo;

	@NotBlank(message = "Address is requried")
	private String address;

	@NotNull
	private Double balance;

	@JsonIgnore
	private LocalDate date = LocalDate.now();

	@PrePersist // Run the method below before the entity is saved
	public void onCreate() {
		this.date = LocalDate.now();
	}

}
