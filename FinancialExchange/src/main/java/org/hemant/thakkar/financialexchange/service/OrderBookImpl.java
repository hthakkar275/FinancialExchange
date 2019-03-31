package org.hemant.thakkar.financialexchange.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderStatus;
import org.hemant.thakkar.financialexchange.domain.OrderType;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.Side;
import org.hemant.thakkar.financialexchange.domain.Trade;
import org.hemant.thakkar.financialexchange.domain.TradeImpl;
import org.hemant.thakkar.financialexchange.repository.OrderRepository;
import org.hemant.thakkar.financialexchange.repository.TradeRepository;

public class OrderBookImpl implements OrderBook {
	private static BigDecimal TWO = new BigDecimal("2.0");
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");
	
	private OrderRepository orderRepository;
	private TradeRepository tradeRepository;
	private Product product;
	private List<Order> orders = new ArrayList<Order>();
	private BigDecimal tickSize;
	
	public OrderBookImpl(Product product, OrderRepository orderRepository, 
			TradeRepository tradeRepository) {
		this(new BigDecimal("0.01"), product, orderRepository, tradeRepository);
	}

	public OrderBookImpl(BigDecimal tickSize, Product product, OrderRepository orderRepository, 
			TradeRepository tradeRepository) {
		this.tickSize = tickSize;
		this.setProduct(product);
		this.orderRepository = orderRepository;
		this.tradeRepository = tradeRepository;
	}
	
	/**
	 * Clips price according to tickSize
	 * 
	 * @param price
	 * @return
	 */
	private BigDecimal clipPrice(BigDecimal price) {
		int numDecPlaces = (int) Math.log10(1 / this.tickSize.doubleValue());
		BigDecimal rounded = price.setScale(numDecPlaces, RoundingMode.HALF_UP);
		return rounded;
	}
	
	
	public void processOrder(Order incomingOrder) {		
		if (incomingOrder.getQuantity() <= 0 ) {
			throw new IllegalArgumentException("processOrder() given qty <= 0");
		}
		
		if (!(incomingOrder.getSide() == Side.BUY || incomingOrder.getSide() == Side.SELL)) {		
			throw new IllegalArgumentException("order neither market nor limit: " + 
					incomingOrder.getSide());
		}

		if (incomingOrder.getType() == OrderType.LIMIT) {
			BigDecimal clippedPrice = clipPrice(incomingOrder.getPrice());
			incomingOrder.setPrice(clippedPrice);
			processLimitOrder(incomingOrder);
		} else {
			processMarketOrder(incomingOrder);
		}
	}
	
	
	private void processMarketOrder(Order incomingOrder) {
		Side side = incomingOrder.getSide();
		int qtyRemaining = incomingOrder.getQuantity();
		if (side == Side.BUY) {
			List<Order> offersByBestPrice = this.orders.stream()
					.filter(o -> o.getSide() == Side.SELL && o.getType() != OrderType.MARKET)
					.sorted(Comparator.comparing(Order::getPrice)
							.thenComparing(Order::getEntryTime))
					.collect(Collectors.toList());
			while ((qtyRemaining > 0) && (offersByBestPrice.size() > 0)) {
				// Find the best offer to match the buy order 
				// That would be to find the sell orders asking for smallest price.
				//List<Order> ordersAtMinPrice = offers.stream().sorted(Comparators.)
				qtyRemaining = processOrderList(offersByBestPrice, qtyRemaining,
												incomingOrder);
			}
		} else if (side == Side.SELL) {
			List<Order> bidsByBestPrice = this.orders.stream()
					.filter(o -> o.getSide() == Side.BUY && o.getType() != OrderType.MARKET)
					.sorted(Comparator.comparing(Order::getPrice)
							.reversed()
							.thenComparing(Order::getEntryTime))
					.collect(Collectors.toList());
			while ((qtyRemaining > 0) && (bidsByBestPrice.size() > 0)) {
				// Find the best offer to match the buy order 
				// That would be to find the sell orders asking for smallest price.
				//List<Order> ordersAtMinPrice = offers.stream().sorted(Comparators.)
				qtyRemaining = processOrderList(bidsByBestPrice, qtyRemaining,
												incomingOrder);
			}
		} else {
			throw new IllegalArgumentException("order neither market nor limit: " + 
				    						    side);
		}
		if (incomingOrder.getTradedQantity() > 0 && incomingOrder.getTradedQantity() < incomingOrder.getQuantity()) {
			incomingOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
		} else if (incomingOrder.getTradedQantity() == incomingOrder.getQuantity()) {
			incomingOrder.setStatus(OrderStatus.FILLED);
		} else {
			incomingOrder.setStatus(OrderStatus.NOT_FILLED);
		}
		orderRepository.saveOrder(incomingOrder);
	}
	
	
	private void processLimitOrder(Order incomingOrder) {
		int qtyRemaining = incomingOrder.getQuantity();
	
		List<Order> tradableOrders = this.orders.stream()
				.filter(o -> {
					boolean match = false;
					if (incomingOrder.getSide() == Side.BUY) {
						match = o.getSide() == Side.SELL && incomingOrder.getPrice().compareTo(o.getPrice()) >= 0;
					} else {
						match = o.getSide() == Side.BUY && incomingOrder.getPrice().compareTo(o.getPrice()) <= 0;
					}
					return match;
				 })
				.sorted(Comparator.comparing(Order::getEntryTime))
				.collect(Collectors.toList());

		while ((tradableOrders.size() > 0) && 
				(qtyRemaining > 0)) {
			qtyRemaining = processOrderList(tradableOrders, qtyRemaining, incomingOrder);
		}
		
		List<Order> tradedOrders = orders.stream()
				.filter(o -> o.getBookedQuantity() == 0)
				.collect(Collectors.toList());
		tradedOrders.stream().forEach(o -> orders.remove(o)); 
		
		incomingOrder.setBookedQuantity(incomingOrder.getQuantity() - incomingOrder.getTradedQantity());
		if (incomingOrder.getTradedQantity() > 0 && incomingOrder.getBookedQuantity() > 0) {
			incomingOrder.setStatus(OrderStatus.PARTIALLY_BOOKED_FILLED);
		} else if (incomingOrder.getTradedQantity() == 0 && incomingOrder.getBookedQuantity() > 0) {
			incomingOrder.setStatus(OrderStatus.BOOKED);
		} else if (incomingOrder.getTradedQantity() == incomingOrder.getQuantity()) {
			incomingOrder.setStatus(OrderStatus.FILLED);
		} else {
			incomingOrder.setStatus(OrderStatus.UNKNOWN);
		}
		if (incomingOrder.getBookedQuantity() > 0) {
			this.orders.add(incomingOrder);
		}
		orderRepository.saveOrder(incomingOrder);

	}
	
	
	private int processOrderList(List<Order> tradableOrders, int qtyRemaining, Order incomingOrder) {
		Iterator<Order> iterator = tradableOrders.iterator();
		
		while ((iterator.hasNext()) && (qtyRemaining > 0)) {
			int qtyTraded = 0;
			Order headOrder = iterator.next();
			if (qtyRemaining < headOrder.getQuantity()) {
				qtyTraded = qtyRemaining;
				qtyRemaining = 0;
				headOrder.setStatus(OrderStatus.PARTIALLY_BOOKED_FILLED);
			} else {
				qtyTraded = headOrder.getQuantity();
				qtyRemaining -= qtyTraded;
				headOrder.setStatus(OrderStatus.FILLED);
				iterator.remove();
			}
			headOrder.setTradedQuantity(headOrder.getTradedQantity() + qtyTraded);
			headOrder.setBookedQuantity(headOrder.getBookedQuantity() - qtyTraded);
			incomingOrder.setTradedQuantity(incomingOrder.getTradedQantity() + qtyTraded);
			Trade trade = new TradeImpl();
			if (incomingOrder.getSide() == Side.SELL) {
				trade.setSeller(incomingOrder);
				trade.setBuyer(headOrder);
			} else {
				trade.setSeller(headOrder);
				trade.setBuyer(incomingOrder);
			}
			trade.setPrice(headOrder.getPrice());
			trade.setQuantity(qtyTraded);
			tradeRepository.saveTrade(trade);
			orderRepository.saveOrder(headOrder);
			System.out.println(trade);
		}
		return qtyRemaining;
	}
	
	public synchronized Order cancelOrder(long orderId) {
		Iterator<Order> iterator = orders.iterator();
		Order cancelledOrder = null;
		while (iterator.hasNext()) {
			Order o = iterator.next();
			if (o.getId() == orderId) {
				cancelledOrder = o;
				iterator.remove();
				if (o.getStatus() == OrderStatus.PARTIALLY_BOOKED_FILLED) {
					o.setStatus(OrderStatus.PARTIALLY_FILLED);
				}
			}
		}
		return cancelledOrder;
	}
	
	
	public void modifyOrder(int qId, HashMap<String, String> quote) {
		// TODO implement modify order
		// Remember if price is changed must check for clearing.
	}
	
	
	public int getVolumeAtPrice(Side side, BigDecimal price) {
	    final BigDecimal clippedPrice = clipPrice(price);
		int volume = orders.stream().filter(o -> o.getSide() == side && o.getPrice().equals(clippedPrice))
				.mapToInt(o -> o.getQuantity()).sum();
		return volume;
		
	}
	
	public BigDecimal getBestBid() {
		BigDecimal bestBid = BigDecimal.ZERO;
		Optional<Order> bestBidOrder = orders.stream()
				.filter(o -> o.getSide() == Side.BUY)
				.sorted(Comparator.comparing(Order::getPrice).reversed()).findFirst();
		if (bestBidOrder.isPresent()) {
			bestBid = bestBidOrder.get().getPrice();
		}
		return bestBid;
	}
	
	public BigDecimal getWorstBid() {
		BigDecimal worstBid = BigDecimal.ZERO;
		Optional<Order> worstBidOrder = orders.stream()
				.filter(o -> o.getSide() == Side.BUY)
				.sorted(Comparator.comparing(Order::getPrice)).findFirst();
		if (worstBidOrder.isPresent()) {
			worstBid = worstBidOrder.get().getPrice();
		}
		return worstBid;
	}
	
	public BigDecimal getBestOffer() {
		BigDecimal bestOffer = BigDecimal.ZERO;
		Optional<Order> bestOfferOrder = orders.stream()
				.filter(o -> o.getSide() == Side.SELL)
				.sorted(Comparator.comparing(Order::getPrice)).findFirst();
		if (bestOfferOrder.isPresent()) {
			bestOffer = bestOfferOrder.get().getPrice();
		}
		return bestOffer;
	}
	
	public BigDecimal getWorstOffer() {
		BigDecimal worstOffer = BigDecimal.ZERO;
		Optional<Order> worstOfferOrder = orders.stream()
				.filter(o -> o.getSide() == Side.SELL)
				.sorted(Comparator.comparing(Order::getPrice).reversed()).findFirst();
		if (worstOfferOrder.isPresent()) {
			worstOffer = worstOfferOrder.get().getPrice();
		}
		return worstOffer;
	}
	
	public int volumeOnSide(Side side) {
		if (side != Side.BUY && side != Side.SELL) {
			throw new IllegalArgumentException("order neither market nor limit: " + 
					side);
		}
		int volume = orders.stream()
						.filter(o -> o.getSide() == side)
						.mapToInt(o -> o.getQuantity())
						.sum();
		return volume;
	}
	
	public BigDecimal getTickSize() {
		return tickSize;
	}
	
	public BigDecimal getSpread() {
		BigDecimal minOfferPrice = BigDecimal.ZERO;
		Optional<BigDecimal> minOfferPriceOpt = orders.stream()
							  						.filter(o -> o.getSide() == Side.SELL)
							  						.min(Comparator.comparing(Order::getPrice))
							  						.map(o -> o.getPrice());
		if (minOfferPriceOpt.isPresent()) {
			minOfferPrice = minOfferPriceOpt.get();
		}
			
		BigDecimal maxBidPrice = BigDecimal.ZERO;
		Optional<BigDecimal> maxBidPriceOpt = orders.stream()
							  						.filter(o -> o.getSide() == Side.BUY)
							  						.min(Comparator.comparing(Order::getPrice))
							  						.map(o -> o.getPrice());
		if (maxBidPriceOpt.isPresent()) {
			maxBidPrice = maxBidPriceOpt.get();
		}

		return minOfferPrice.subtract(maxBidPrice);
	}
	
	public synchronized BigDecimal getMid() {
		return this.getBestBid().add(this.getSpread().divide(TWO));
	}
	
	public boolean bidsAndAsksExist() {
		long bidCount = orders.stream().filter(o -> o.getSide() == Side.BUY).count();
		long offerCount = orders.stream().filter(o -> o.getSide() == Side.SELL).count();
		return bidCount > 0 && offerCount > 0;
	}
	
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("Time: " + formatter.format(LocalDateTime.now()) + "\n");
		message.append("-------- The Order Book --------\n");
		message.append("  ").append(this.product).append("\n");
		message.append("   ------- Bid  Book --------   \n");
		String bids = this.orders.stream().filter(o -> o.getSide() == Side.BUY)
				.map(o -> o.toString())
				.reduce("", (a, b) -> a + "\n    " + b);
		message.append(bids + "\n");
		message.append("   ------- Offer  Book --------   \n");
		String offers = this.orders.stream().filter(o -> o.getSide() == Side.SELL)
				.map(o -> o.toString())
				.reduce("", (a, b) -> a + "\n    " + b);
		message.append(offers + "\n");
		message.append("---------------------------------\n");
		return message.toString();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public Order getOrder(long orderId) {
		Order order = orders.stream()
			.filter(o -> o.getId() == orderId)
			.findFirst()
			.orElse(null);
		return order;
	}

	
}