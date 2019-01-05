package org.hemant.thakkar.financialexchange.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderBookService")
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("productMemoryRepositoryImp")
	private ProductRepository productRepository;
	
	Map<Long, OrderBook> orderBooks;
	
	public OrderBookServiceImpl() {
		this.orderBooks = new ConcurrentHashMap<Long, OrderBook>();
	}
	
	@Override
	public OrderBook getOrderBook(long productId) {
		OrderBook orderBook = orderBooks.get(productId);
		if (orderBook == null) {
			final OrderBook newOrderBook = new OrderBookImpl(productRepository.getProduct(productId));
			List<Order> orders = orderRepository.getOrdersByProduct(productId);
			orders.stream().forEach(o -> newOrderBook.processOrder(o, false));
			orderBooks.put(productId, orderBook);
			orderBook = newOrderBook;
		}
		return orderBook;
	}

	@Override
	public void deleteOrderBook(long productId) {
		// TODO Auto-generated method stub

	}

}
