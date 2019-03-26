package org.hemant.thakkar.financialexchange.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderState {
	private List<Trade> trades = new ArrayList<Trade>();
	private boolean orderInBook = false;
	private Order order;
	
	public OrderState(List<Trade> trades, 
					   boolean orderInBook) {
		this.trades = trades;
		this.orderInBook = orderInBook;
	}

	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public boolean isOrderInBook() {
		return orderInBook;
	}
	
	public String toString() {
		String retString = "--- Order Report ---:\nTrades:\n";
		for (Trade t : trades) {
			retString += ("\n" + t.toString());
		}
		retString += ("order in book? " + orderInBook + "\n");
		retString+= ("\nOrders:\n");
		retString += (order.toString());
		return  retString + "\n--------------------------";
	}
}
