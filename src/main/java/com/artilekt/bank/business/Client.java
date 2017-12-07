package com.artilekt.bank.business;

import java.io.Serializable;
import java.util.UUID;

public class Client implements Serializable {

	private String firstName;
	private String lastName;
	private String driverLicense;
	
	
	public Client(String firstName, String lastName, String driverLicense) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.driverLicense = driverLicense;
	}
	
	public Client() {}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDriverLicense() {
		return driverLicense;
	}
	
	
}
