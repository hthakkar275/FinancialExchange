package org.hemant.thakkar.financialexchange.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderBookState;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderImpl;
import org.hemant.thakkar.financialexchange.domain.OrderLongevity;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.ParticipantRepository;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderBookServiceImpl")
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("productMemoryRepositoryImpl")
	private ProductRepository productRepository;
	
	@Autowired
	@Qualifier("participantMemoryRepositoryImpl")
	private ParticipantRepository participantRepository;
	
	Map<Long, OrderBook> orderBooks;
	
	public OrderBookServiceImpl() {
		this.orderBooks = new ConcurrentHashMap<Long, OrderBook>();
	}
	
	public OrderBook getOrderBook(long productId) {
		OrderBook orderBook = orderBooks.get(productId);
		if (orderBook == null) {
			final OrderBook newOrderBook = new OrderBookImpl(productRepository.getProduct(productId));
			List<Order> orders = orderRepository.getOrdersByProduct(productId);
			if (orders != null) {
				orders.stream().forEach(o -> newOrderBook.processOrder(o, false));
			}
			orderBooks.put(productId, newOrderBook);
			orderBook = newOrderBook;
		}
		return orderBook;
	}

	public void deleteOrderBook(long productId) {
		orderBooks.remove(productId);
	}

	public void addOrder(OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		OrderBook orderBook = getOrderBook(order.getProduct().getId());
		orderBook.processOrder(order, false);
	}

	public void cancelOrder(Order order) {
		OrderBook orderBook = getOrderBook(order.getProduct().getId());
		orderBook.cancelOrder(order.getId());
	}

	private Order createOrder(OrderEntry orderEntry) throws ExchangeException {
		Product product = productRepository.getProduct(orderEntry.getProductId());
		if (product == null) {
			throw new ExchangeException(ResultCode.PRODUCT_NOT_FOUND);
		}
		
		Participant participant = participantRepository.getParticipant(orderEntry.getParticipantId());
		if (participant == null) {
			throw new ExchangeException(ResultCode.PARTICIPANT_NOT_FOUND);
		}
		
		Order order = new OrderImpl();
		order.setProduct(product);
		order.setParticipant(participant);
		order.setType(orderEntry.getType());
		order.setLongevity(OrderLongevity.DAY);
		order.setSide(orderEntry.getSide());
		order.setQuantity(orderEntry.getQuantity());
		order.setPrice(orderEntry.getPrice());
		return order;
	}

	@Override
	public void cancelOrder(long orderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrderBookState getOrderBookMontage(long productId) {
		// TODO Auto-generated method stub
		return null;
	}

}

