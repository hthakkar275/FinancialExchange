package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderReport;

import java.util.List;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;

public interface OrderManagementService {
	void acceptNewOrder(OrderEntry orderEntry) throws ExchangeException;
	void cancelOrder(long orderId) throws ExchangeException;
	OrderReport getOrderStatus(long orderId) throws ExchangeException;
	void updateFromOrderBook(long orderId, OrderEntry orderEntry) throws ExchangeException;
	List<OrderReport> getOrdersForProduct(long productId) throws ExchangeException;
}

