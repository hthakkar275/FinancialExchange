package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.ProductEntry;

public interface ProductManagementService {
	Product addProduct(ProductEntry productEntry) throws ExchangeException;
	Product getProduct(long productId) throws ExchangeException;
	void deleteProduct(long productId) throws ExchangeException;
	Product updateProduct(long productId, ProductEntry productEntry) throws ExchangeException;
}

