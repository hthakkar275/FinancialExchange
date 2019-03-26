package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.OrderBookState;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;

public interface OrderBookService {
	OrderBook getOrderBook(long productId);
	void deleteOrderBook(long productId);
	void addOrder(OrderEntry orderEntry) throws ExchangeException;
	void cancelOrder(long orderId);
	OrderBookState getOrderBookMontage(long productId);
}

