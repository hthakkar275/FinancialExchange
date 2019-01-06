package org.hemant.thakkar.financialexchange.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.domain.Equity;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.springframework.stereotype.Service;

@Service("productMemoryRepositoryImpl")
public class ProductMemoryRepositoryImpl implements ProductRepository {

	Map<Long, Product> products; 
	
	public ProductMemoryRepositoryImpl() {
		products = new ConcurrentHashMap<Long, Product>();
		createProducts();
	}
	
	@Override
	public long saveProduct(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteProduct(long productId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Product getProduct(long productId) {
		return products.get(productId);
	}

	private void createProducts() {
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		products.put(equity.getId(), equity);
	}
}
