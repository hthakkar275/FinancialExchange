package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.OrderReport;

public interface OrderManagementService {
	OrderReport acceptNewOrder(OrderEntry orderEntry) throws ExchangeException;
	OrderReport cancelOrder(long orderId) throws ExchangeException;
	OrderReport getOrderStatus(long orderId) throws ExchangeException;
	OrderReport updateOrder(long orderId, OrderEntry orderEntry) throws ExchangeException;
}

