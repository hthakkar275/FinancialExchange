package org.hemant.thakkar.financialexchange.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.domain.Order;
import org.springframework.stereotype.Service;

@Service("orderMemoryRepositoryImpl")
public class OrderMemoryRepositoryImpl implements OrderRepository {

	private static AtomicLong idGenerator = new AtomicLong(1);
	Map<Long, Order> orders;
	
	public OrderMemoryRepositoryImpl() {
		this.orders = new ConcurrentHashMap<>();
	}
	
	@Override
	public long saveOrder(Order order) {
		if (order.getId() == 0) {
			order.setId(idGenerator.getAndIncrement());
		}
		orders.put(order.getId(), order);
		return order.getId();
	}

	@Override
	public boolean deleteOrder(long orderId) {
		Order order = orders.remove(orderId);
		return order != null;
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
		List<Order> ordersByParticipant = 
				orders.values().stream()
				.parallel()
				.filter(o -> o.getParticipant().getId() == participantId)
				.collect(Collectors.toList());
		return ordersByParticipant;
	}

	@Override
	public int getCount() {
		return orders.size();
	}
	
}
