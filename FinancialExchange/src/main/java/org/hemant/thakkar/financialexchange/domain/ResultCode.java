package org.hemant.thakkar.financialexchange.domain;

public enum ResultCode {

	PRODUCT_NOT_FOUND(1001, "Product not found"),
	PARTICIPANT_NOT_FOUND(1002, "Participant not found"),
	ORDER_ACCEPTED(1003, "Order accepted"),
	ORDER_REJECTED(1004, "Order rejected"), 
	PRODUCT_REJECTED(1005, "Order rejected"), 
	PRODUCT_ADDED(1006, "Product added"),
	PARTICIPANT_ADDED(1007, "Participant added"),
	UNSUPPORTED_ENTITY(9000, "Operation on an unsupported entity"),
	GENERAL_ERROR(999999, "General error. Contact Exchange");
	
	ResultCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private String message;
	private int code;
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
}

