package com.artilekt.bank.business;

import java.io.Serializable;
import java.util.UUID;

public class Asset implements Serializable {
	
	public Asset() {}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asset other = (Asset) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	
	
	private String type;
	private String description;
	private String year;
    private Double value;
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Double getValue() {
		return value;
	}
	public Double setValue(Double value) {
		this.value = value;
		return this.value;
	}
	
	public void increaseValue(double valueIncrease) {
		this.value += valueIncrease;
	}
	
	public void decreaseValue(double valueDecrease) {
		this.value -= valueDecrease;
	}
}
