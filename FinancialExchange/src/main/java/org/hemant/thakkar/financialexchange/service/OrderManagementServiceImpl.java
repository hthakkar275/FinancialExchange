package org.hemant.thakkar.financialexchange.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderImpl;
import org.hemant.thakkar.financialexchange.domain.OrderLongevity;
import org.hemant.thakkar.financialexchange.domain.OrderReport;
import org.hemant.thakkar.financialexchange.domain.OrderStatus;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.domain.Trade;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.ParticipantRepository;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.hemant.thakkar.financialexchange.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderManagementServiceImpl")
public class OrderManagementServiceImpl implements OrderManagementService {

	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");
	
	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("orderBookServiceImpl")
	private OrderBookService orderBookService;
	
	@Autowired
	@Qualifier("productMemoryRepositoryImpl")
	private ProductRepository productRepository;
	
	@Autowired
	@Qualifier("participantMemoryRepositoryImpl")
	private ParticipantRepository participantRepository;

	@Autowired
	@Qualifier("tradeMemoryRepositoryImpl")
	private TradeRepository tradeRepository;

	@Override
	public void cancelOrder(long orderId) throws ExchangeException {
		Order order = orderRepository.getOrder(orderId);
		if (order == null) {
			throw new ExchangeException(ResultCode.ORDER_NOT_FOUND);
		}
		if (order.getStatus() == OrderStatus.FILLED) {
			throw new ExchangeException(ResultCode.ORDER_FILLED);
		}
		orderBookService.cancelOrder(orderId);
	}

	@Override
	public OrderReport getOrderStatus(long orderId) throws ExchangeException {
		Order order = orderRepository.getOrder(orderId);
		OrderReport orderReport = createOrderReport(order);
		return orderReport;
	}

	@Override
	public long acceptNewOrder(OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		long orderId = orderRepository.saveOrder(order);
		orderEntry.setId(orderId);
		orderBookService.addOrder(orderEntry);
		return order.getId();
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
	public void updateFromOrderBook(long orderId, OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		orderRepository.saveOrder(order);
	}

	private OrderReport createOrderReport(Order order) {
		OrderReport orderReport = new OrderReport();
		orderReport.setBookedQuantity(order.getBookedQuantity());
		orderReport.setEntryTime(timeFormatter.format(order.getEntryTime()));
		orderReport.setId(order.getId());
		orderReport.setParticipantId(order.getParticipant().getId());
		orderReport.setLongevity(order.getLongevity());
		orderReport.setOriginalQuantity(order.getQuantity());
		orderReport.setProductId(order.getProduct().getId());
		orderReport.setSide(order.getSide());
		orderReport.setStatus(order.getStatus());
		orderReport.setTradedQuantity(order.getTradedQantity());
		orderReport.setType(order.getType());
		orderReport.setPrice(order.getPrice());
		List<Long> trades = tradeRepository.getTrades(order.getId())
				.stream()
				.map(Trade::getId)
				.collect(Collectors.toList());
		orderReport.setTrades(trades);
		return orderReport;
	}

	@Override
	public List<OrderReport> getOrdersForProduct(long productId) throws ExchangeException {
		List<Order> orders = orderRepository.getOrdersByProduct(productId);
		List<OrderReport> orderReports = orders.stream()
				.map(this::createOrderReport)
				.collect(Collectors.toList());
		return orderReports;
	}
}
