package org.hemant.thakkar.financialexchange.repository;

import java.util.List;

import org.hemant.thakkar.financialexchange.domain.Trade;
import org.springframework.stereotype.Service;

@Service("tradeMemoryRepositoryImpl")
public class TradeMemoryRepositoryImpl implements TradeRepository {

	@Override
	public long saveTrade(Trade trade) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteTrade(Trade trade) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Trade getTrade(long tradeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Trade> getTrades(long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

}

