package org.hemant.thakkar.financialexchange.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.domain.Order;
import org.springframework.stereotype.Service;

@Service("orderMemoryRepositoryImpl")
public class OrderMemoryRepositoryImpl implements OrderRepository {

	Map<Long, Order> orders;
	
	public OrderMemoryRepositoryImpl() {
		this.orders = new ConcurrentHashMap<>();
	}
	
	@Override
	public long saveOrder(Order order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteOrder(long orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Order getOrder(long orderId) {
		return orders.get(orderId);
	}

	@Override
	public List<Order> getOrdersByProduct(long productId) {
		List<Order> ordersByProduct = 
				orders.values().stream()
					.parallel()
					.filter(o -> o.getProduct().getId() == productId)
					.collect(Collectors.toList());
		return ordersByProduct;
	}

	@Override
	public List<Order> getOrdersByParticipant(long participantId) {
		// TODO Auto-generated method stub
		return null;
	}

}
