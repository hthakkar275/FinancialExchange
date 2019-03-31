package org.hemant.thakkar.financialexchange.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderBookState;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.ParticipantRepository;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.hemant.thakkar.financialexchange.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderBookServiceImpl")
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("tradeMemoryRepositoryImpl")
	private TradeRepository tradeRepository;

	@Autowired
	@Qualifier("productMemoryRepositoryImpl")
	private ProductRepository productRepository;
	
	@Autowired
	@Qualifier("participantMemoryRepositoryImpl")
	private ParticipantRepository participantRepository;
	
	private Map<Long, OrderBook> orderBooks;
	
	public OrderBookServiceImpl() {
		this.orderBooks = new ConcurrentHashMap<Long, OrderBook>();
	}
	
	public OrderBook getOrderBook(long productId) throws ExchangeException {
		OrderBook orderBook = null;
		orderBook = orderBooks.get(productId);
		if (orderBook == null) {
			orderBook = new OrderBookImpl(productRepository.getProduct(productId), 
					orderRepository, tradeRepository);
			orderBooks.put(productId, orderBook);
		}
		return orderBook;
	}
		
	public void deleteOrderBook(long productId) {
		orderBooks.remove(productId);
	}

	public void addOrder(OrderEntry orderEntry) throws ExchangeException {
		Order order = orderRepository.getOrder(orderEntry.getId());
		OrderBook orderBook = getOrderBook(order.getProduct().getId());
		orderBook.processOrder(order);
	}

	public void cancelOrder(Order order) throws ExchangeException {
		// TODO
	}

	@Override
	public void cancelOrder(long orderId) throws ExchangeException {
		Order order = orderRepository.getOrder(orderId);
		Product product = order.getProduct();
		OrderBook orderBook = getOrderBook(product.getId());
		orderBook.cancelOrder(orderId);		
	}

	@Override
	public OrderBookState getOrderBookMontage(long productId) {
		// TODO Auto-generated method stub
		return null;
	}

}

