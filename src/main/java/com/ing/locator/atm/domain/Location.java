package com.ing.locator.atm.domain;

public class Location {
	
	private String distance;
	private String type;
	private Address address;
	
	public String getDistance() {
		return distance;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
