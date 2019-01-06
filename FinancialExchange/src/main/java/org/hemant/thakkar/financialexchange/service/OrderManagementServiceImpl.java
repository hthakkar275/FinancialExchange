package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderImpl;
import org.hemant.thakkar.financialexchange.domain.OrderLongevity;
import org.hemant.thakkar.financialexchange.domain.OrderReport;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.ParticipantRepository;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderManagementServiceImpl")
public class OrderManagementServiceImpl implements OrderManagementService {

	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("orderBookServiceImpl")
	OrderBookService orderBookService;
	
	@Autowired
	@Qualifier("productMemoryRepositoryImpl")
	ProductRepository productRepository;
	
	@Autowired
	@Qualifier("participantMemoryRepositoryImpl")
	ParticipantRepository participantRepository;

	@Override
	public OrderReport cancelOrder(long orderId) throws ExchangeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderReport getOrderStatus(long orderId) throws ExchangeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderReport acceptNewOrder(OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		orderRepository.saveOrder(order);
		OrderBook orderBook = orderBookService.getOrderBook(orderEntry.getProductId());
		if (orderBook == null) {
			throw new ExchangeException(ResultCode.GENERAL_ERROR);
		}
		OrderReport orderReport = orderBook.processOrder(order, false);
		return orderReport;
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


}
