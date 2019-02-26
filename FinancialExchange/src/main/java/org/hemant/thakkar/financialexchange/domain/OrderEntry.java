package org.hemant.thakkar.financialexchange.domain;

import java.math.BigDecimal;

public class OrderEntry {

	private Side side;
	private int quantity;
	private BigDecimal price;
	private OrderStatus status;
	private OrderType type;
	private OrderLongevity longevity;
	private long productId;
	private long participantId;
	
	public Side getSide() {
		return side;
	}
	public void setSide(Side side) {
		this.side = side;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public OrderType getType() {
		return type;
	}
	public void setType(OrderType type) {
		this.type = type;
	}
	public OrderLongevity getLongevity() {
		return longevity;
	}
	public void setLongevity(OrderLongevity longevity) {
		this.longevity = longevity;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
 	
}
