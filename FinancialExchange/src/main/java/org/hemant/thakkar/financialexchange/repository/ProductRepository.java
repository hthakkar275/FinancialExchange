package org.hemant.thakkar.financialexchange.repository;

import java.util.List;

import org.hemant.thakkar.financialexchange.domain.Product;

public interface ProductRepository {
	long saveProduct(Product product);
	boolean deleteProduct(long productId);
	Product getProduct(long productId);
	Product getProduct(String symbol);
	int getCount();
	List<Product> getProducts();
}

