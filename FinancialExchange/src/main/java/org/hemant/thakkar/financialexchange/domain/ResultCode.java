package org.hemant.thakkar.financialexchange.domain;

public enum ResultCode {

	PRODUCT_ADDED(1001, "Product added"),
	PRODUCT_REJECTED(1002, "Order rejected"), 
	PRODUCT_FOUND(1003, "Product found"),
	PRODUCT_NOT_FOUND(1004, "Product not found"),
	PRODUCT_UPDATED(1005, "Product updated"),
	PRODUCT_DELETED(1006, "Product deleted"),
	PARTICIPANT_ADDED(1007, "Participant added"),
	PARTICIPANT_REJECTED(1008, "Participant rejected"),
	PARTICIPANT_FOUND(1009, "Participant found"),
	PARTICIPANT_NOT_FOUND(1010, "Participant not found"),
	PARTICIPANT_UPDATED(1011, "Participant updated"),
	PARTICIPANT_DELETED(1012, "Participant deleted"),
	ORDER_ACCEPTED(1013, "Order accepted"),
	ORDER_REJECTED(1014, "Order rejected"), 
	ORDER_FOUND(1015, "Order found"),
	ORDER_NOT_FOUND(1016, "Order not found"),
	ORDER_CANCELLED(1017, "Order cancelled"),
	ORDER_FILLED(1018, "Order filled"),
	ORDER_UPDATED(1019, "Order updated"),
	ORDER_BOOK_FOUND(1020, "Order book found"),
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

