package com.stitch_house_market.liqpay_invoice_maker;

import java.util.Currency;

public class Invoice {
	private String phoneNumber;
	private String email;
	private String description;
	private double price;
	private Currency currency;
	private boolean testMode;
	
	public Invoice(String email, double price, Currency currency) {
		if (isNullOrEmpty(email) || price <= 0 || isNull(currency)) {
			throw new IllegalArgumentException();
		}
		
		this.email = email;
		this.price = price;
		this.currency = currency;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		if (isNullOrEmpty(phoneNumber)) {
			throw new IllegalArgumentException();
		}
		
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		if (isNullOrEmpty(description)) {
			throw new IllegalArgumentException();
		}
		
		this.description = description;
	}
	
	public double getPrice() {
		return price;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public boolean isTestMode() {
		return testMode;
	}
	
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	@Override
	public String toString() {
		return "Invoice [phoneNumber=" + phoneNumber + ", email=" + email + ", description=" + description + ", price="
				+ price + ", testMode=" + testMode + "]";
	}
	
	private boolean isNullOrEmpty(String string) {
		if (!isNull(string) && !string.trim().isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	private boolean isNull(Object object) {
		return object == null;
	}
}
