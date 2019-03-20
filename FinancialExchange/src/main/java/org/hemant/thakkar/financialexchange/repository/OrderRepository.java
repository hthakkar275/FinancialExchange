package org.hemant.thakkar.financialexchange.repository;

import java.util.List;

import org.hemant.thakkar.financialexchange.domain.Order;

public interface OrderRepository {
	long saveOrder(Order order);
	boolean deleteOrder(long orderId);
	Order getOrder(long orderId);
	List<Order> getOrdersByProduct(long productId);
	List<Order> getOrdersByParticipant(long participantId);
	int getCount();
}

