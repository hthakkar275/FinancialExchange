package org.hemant.thakkar.financialexchange.domain;

public enum OrderStatus {
	BOOKED,
	PARTIALLY_BOOKED_FILLED,
	PARTIALLY_FILLED,
	NOT_FILLED,
	FILLED,
	CANCELLED,
	PARTIALLY_CANCELLED,
	REJECTED
}
