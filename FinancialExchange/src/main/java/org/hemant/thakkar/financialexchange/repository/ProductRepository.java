package org.hemant.thakkar.financialexchange.repository;

import org.hemant.thakkar.financialexchange.domain.Product;

public interface ProductRepository {
	long saveProduct(Product product);
	boolean deleteProduct(long productId);
	Product getProduct(long productId);
	int getCount();
}

