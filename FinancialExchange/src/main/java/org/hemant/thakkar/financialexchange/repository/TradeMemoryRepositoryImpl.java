package org.hemant.thakkar.financialexchange.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.domain.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("tradeMemoryRepositoryImpl")
public class TradeMemoryRepositoryImpl implements TradeRepository {

	private static AtomicLong idGenerator = new AtomicLong(1);
	private Map<Long, Trade> trades; 

	@Autowired
	@Qualifier("orderMemoryRepositoryImpl")
	private OrderRepository orderRepository;

	public TradeMemoryRepositoryImpl() {
		trades = new ConcurrentHashMap<>();
	}
	
	@Override
	public long saveTrade(Trade trade) {
		if (trade.getId() == 0) {
			trade.setId(idGenerator.getAndIncrement());
		}
		trades.put(trade.getId(), trade);
		return trade.getId();
	}

	@Override
	public boolean deleteTrade(long tradeId) {
		Trade trade = trades.remove(tradeId);
		return trade != null;
	}

	@Override
	public Trade getTrade(long tradeId) {
		return trades.get(tradeId);
	}

	@Override
	public List<Trade> getTrades(long orderId) {
		List<Trade> tradesForOrder = trades.values().stream()
			.filter(t -> t.getSeller().getId() == orderId || t.getBuyer().getId() == orderId)
			.collect(Collectors.toList());
		return tradesForOrder;
	}

}

