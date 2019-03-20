package org.hemant.thakkar.financialexchange.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hemant.thakkar.financialexchange.domain.Broker;
import org.hemant.thakkar.financialexchange.domain.Equity;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderImpl;
import org.hemant.thakkar.financialexchange.domain.OrderLongevity;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.Side;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test the in-memory implentation of OrderRepository")
class OrderMemoryRepositoryImplTest {

	private static List<Product> products;
	
	static {
		products = new ArrayList<>();
    	Product equity1 = new Equity();
    	equity1.setDescription("Aaple Inc");
    	equity1.setId(1);
    	equity1.setSymbol("AAPL");
    	products.add(equity1);
    	
    	Product equity2 = new Equity();
    	equity2.setDescription("Google Inc");
    	equity2.setId(2);
    	equity2.setSymbol("GOOG");
    	products.add(equity2);

    	Product equity3 = new Equity();
    	equity3.setDescription("IBM Inc");
    	equity3.setId(3);
    	equity3.setSymbol("IBM");
    	products.add(equity3);
	}

	private static List<Participant> participants;
	
	static {
		participants = new ArrayList<>();
    	Participant p1 = new Broker();
    	p1.setName("Hemant Thakkar");
    	p1.setId(1);
    	participants.add(p1);
    	
    	Participant p2 = new Broker();
    	p2.setName("John Smith");
    	p2.setId(2);
    	participants.add(p2);

    	Participant p3 = new Broker();
    	p3.setName("Jane Doe");
    	p3.setId(3);
    	participants.add(p3);
	}


	private static List<Order> orders;
	
	static {
		orders = new ArrayList<>();
    	Order order1 = new OrderImpl();
    	order1.setLongevity(OrderLongevity.DAY);
    	order1.setPrice(new BigDecimal("10.00"));
    	order1.setSide(Side.BUY);
    	order1.setProduct(products.get(0));
    	order1.setParticipant(participants.get(0));
    	orders.add(order1);
    	
    	Order order2 = new OrderImpl();
    	order2.setLongevity(OrderLongevity.GTC);
    	order2.setPrice(new BigDecimal("20.00"));
    	order2.setSide(Side.SELL);
    	order2.setProduct(products.get(1));
    	order2.setParticipant(participants.get(1));
    	orders.add(order2);

    	Order order3 = new OrderImpl();
    	order3.setLongevity(OrderLongevity.FILL_OR_KILL);
    	order3.setPrice(new BigDecimal("30.00"));
    	order3.setSide(Side.BUY);
    	order3.setProduct(products.get(2));
    	order3.setParticipant(participants.get(2));
    	orders.add(order3);
	}


	private OrderMemoryRepositoryImpl orderRepository = new OrderMemoryRepositoryImpl();
		
	@DisplayName("Test saving a new order")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testSaveNewOrderProvider")
	void testSaveNewOrder(Order order) {
		long orderId = orderRepository.saveOrder(order);
		Order savedOrder = orderRepository.getOrder(orderId);
		assertEquals(orderId, savedOrder.getId());
		assertEquals(order.getPrice(), savedOrder.getPrice());
		assertEquals(order.getLongevity(), savedOrder.getLongevity());
	}

	@DisplayName("Test saving/updating existing order")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndOrderProvider")
	void testUpdateOrder(Order order) {
		
		// Save the order for the first time
		long orderId = orderRepository.saveOrder(order);
		
		// Now update the same order, but with different symbol and description
		// The total count of orders should remain 1 but the update-saved
		// order should have new symbol and description
		Order updatedOrder = new OrderImpl();
		updatedOrder.setId(orderId);
		updatedOrder.setPrice(new BigDecimal("25.00"));
		updatedOrder.setLongevity(OrderLongevity.GTC);
		long updatedOrderId = orderRepository.saveOrder(updatedOrder);
		
		assertEquals(orderId, updatedOrderId);
		assertEquals(1, orderRepository.getCount());

		Order retrievedUpdatedOrder  = orderRepository.getOrder(orderId);
		assertEquals(orderId, retrievedUpdatedOrder.getId());
		assertEquals(updatedOrder.getPrice(), retrievedUpdatedOrder.getPrice());
		assertEquals(updatedOrder.getLongevity(), retrievedUpdatedOrder.getLongevity());
		assertNotEquals(order.getPrice(), retrievedUpdatedOrder.getPrice());
		assertNotEquals(order.getLongevity(), retrievedUpdatedOrder.getLongevity());
	}

	@DisplayName("Test deleting a order that exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndOrderProvider")
	void testDeleteExistingOrder(Order order) {
		// Save the order for the first time
		long orderId = orderRepository.saveOrder(order);
		assertNotEquals(orderId, 0);
		
		boolean deleted = orderRepository.deleteOrder(orderId);
		assertTrue(deleted);
	}

	@DisplayName("Test deleting a order that does not exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndOrderProvider")
	void testDeleteNonExistingOrder(Order order) {
		boolean deleted = orderRepository.deleteOrder(order.getId());
		assertFalse(deleted);
	}

	@DisplayName("Test retrieving a order that does not exists")
	@Test
	void testGetNonExistingOrder() {
		Order order = orderRepository.getOrder(1000);
		assertNull(order);
	}

	@DisplayName("Test retrieving order by product")
	@Test
	void testGetOrderByProduct() {
		orderRepository.saveOrder(orders.get(0));
		orderRepository.saveOrder(orders.get(1));
		orderRepository.saveOrder(orders.get(2));
		
		List<Order> ordersForProduct = orderRepository.getOrdersByProduct(2);
		assertEquals(1, ordersForProduct.size());
		assertEquals(2, ordersForProduct.get(0).getId());
	}
	
	@DisplayName("Test retrieving order by participant")
	@Test
	void testGetOrderByParticipant() {
		orderRepository.saveOrder(orders.get(0));
		orderRepository.saveOrder(orders.get(1));
		orderRepository.saveOrder(orders.get(2));
		
		List<Order> ordersForProduct = orderRepository.getOrdersByParticipant(2);
		assertEquals(1, ordersForProduct.size());
		assertEquals(2, ordersForProduct.get(0).getId());
	}

    static Stream<Arguments> testSaveNewOrderProvider() {
        return Stream.of(
                Arguments.of(orders.get(0)),
                Arguments.of(orders.get(1)),
                Arguments.of(orders.get(2))
        );
    }
    
    static Stream<Arguments> testUpdateAndOrderProvider() {
    	Product equity = new Equity();
    	equity.setDescription("XYZ Inc");
    	equity.setId(5);
    	equity.setSymbol("XYZ");
    	products.add(equity);

    	Participant p1 = new Broker();
    	p1.setName("Michael Smith");
    	p1.setId(6);

    	Order order = new OrderImpl();
    	order.setLongevity(OrderLongevity.DAY);
    	order.setPrice(new BigDecimal("150.00"));
    	order.setSide(Side.BUY);
    	order.setProduct(equity);
    	order.setParticipant(p1);

        return Stream.of(
                Arguments.of(order)
        );
    }

}
