package org.hemant.thakkar.financialexchange.repository;

import org.hemant.thakkar.financialexchange.domain.Product;
import org.springframework.stereotype.Service;

@Service("productMemoryRepositoryImpl")
public class ProductMemoryRepositoryImpl implements ProductRepository {

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
		// TODO Auto-generated method stub
		return null;
	}

}
