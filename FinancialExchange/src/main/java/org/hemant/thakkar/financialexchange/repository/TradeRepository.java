package org.hemant.thakkar.financialexchange.repository;

import java.util.List;

import org.hemant.thakkar.financialexchange.domain.Trade;

public interface TradeRepository {
	long saveTrade(Trade trade);
	long deleteTrade(Trade trade);
	Trade getTrade(long tradeId);
	List<Trade> getTrades(long orderId);
}

