package org.hemant.thakkar.financialexchange.service;

public interface OrderBookService {
	OrderBook getOrderBook(long productId);
	void deleteOrderBook(long productId);
}

