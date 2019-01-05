package org.hemant.thakkar.financialexchange.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderReport;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.Side;
import org.hemant.thakkar.financialexchange.domain.Trade;

public interface OrderBook {
	void reset();

	OrderReport processOrder(Order order, boolean verbose);

	void cancelOrder(long orderId);

	void modifyOrder(int qId, HashMap<String, String> order);

	int getVolumeAtPrice(Side side, BigDecimal price);

	BigDecimal getBestBid();

	BigDecimal getWorstBid();

	BigDecimal getBestOffer();

	BigDecimal getWorstOffer();

	int volumeOnSide(Side side);

	BigDecimal getTickSize();

	BigDecimal getSpread();

	BigDecimal getMid();

	boolean bidsAndAsksExist();

	String toString();

	List<Trade> getTape();

	Product getProduct();

	void setProduct(Product product);

	void setOrders(List<Order> orders);

}
