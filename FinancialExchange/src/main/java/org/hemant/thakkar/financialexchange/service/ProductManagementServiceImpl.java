package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.Equity;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.hemant.thakkar.financialexchange.domain.ProductEntry;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("productManagementServiceImpl")
public class ProductManagementServiceImpl implements ProductManagementService {

	@Autowired
	@Qualifier("productMemoryRepositoryImpl")
	ProductRepository productRepository;
	
	@Override
	public Product addProduct(ProductEntry productEntry) throws ExchangeException {
		Product product = null;
		if (productEntry.getProductType().equals("EQUITY")) {
			product = new Equity();
			product.setSymbol(productEntry.getSymbol());
			product.setDescription(productEntry.getDescription());
			productRepository.saveProduct(product);
		} else {
			throw new ExchangeException(ResultCode.UNSUPPORTED_ENTITY);
		}
		return product;
	}

	@Override
	public void deleteProduct(long productId) throws ExchangeException {
		boolean deleted = productRepository.deleteProduct(productId);
		if (!deleted) {
			Product product = productRepository.getProduct(productId);
			if (product == null) {
				throw new ExchangeException(ResultCode.PRODUCT_NOT_FOUND);
			}
		}
	}

	@Override
	public Product updateProduct(long productId, ProductEntry productEntry) throws ExchangeException {
		Product product = null;
		if (productEntry.getProductType().equals("EQUITY")) {
			product = (Equity) productRepository.getProduct(productId);
			if (product == null) {
				throw new ExchangeException(ResultCode.PRODUCT_NOT_FOUND);
			}
			product.setSymbol(productEntry.getSymbol());
			product.setDescription(productEntry.getDescription());
			product = (Equity) productRepository.getProduct(productId);
		} else {
			throw new ExchangeException(ResultCode.UNSUPPORTED_ENTITY);
		}
		return product;
	}

	@Override
	public Product getProduct(long productId) throws ExchangeException {
		Product product = productRepository.getProduct(productId);
		if (product == null) {
			throw new ExchangeException(ResultCode.PRODUCT_NOT_FOUND);
		}
		return product;
	}

}

