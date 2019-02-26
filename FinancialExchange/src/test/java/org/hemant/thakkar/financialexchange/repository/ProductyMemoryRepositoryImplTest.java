package org.hemant.thakkar.financialexchange.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hemant.thakkar.financialexchange.domain.Equity;
import org.hemant.thakkar.financialexchange.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test the in-memory implentation of ProductRepository")
class ProductyMemoryRepositoryImplTest {

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
	
	private ProductMemoryRepositoryImpl productRepository = new ProductMemoryRepositoryImpl();
		
	@DisplayName("Test saving a new product")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testSaveNewProductProvider")
	void testSaveNewProduct(Product product) {
		long productId = productRepository.saveProduct(product);
		Product savedProduct = productRepository.getProduct(productId);
		assertEquals(productId, savedProduct.getId());
		assertEquals(product.getSymbol(), savedProduct.getSymbol());
		assertEquals(product.getDescription(), savedProduct.getDescription());
	}

	@DisplayName("Test saving/updating existing product")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateProductProvider")
	void testUpdateProduct(Product product) {
		int productCount = productRepository.getProductCount();
		long productId = productRepository.saveProduct(product);
		Product savedProduct = productRepository.getProduct(productId);
		assertEquals(productId, savedProduct.getId());
		assertEquals(product.getSymbol(), savedProduct.getSymbol());
		assertEquals(product.getDescription(), savedProduct.getDescription());
		assertEquals(productCount, productRepository.getProductCount());
	}

	@DisplayName("Test deleting a product that exists")
	@Test
	void testDeleteExistingProduct() {
		fail("Not yet implemented");
	}

	@DisplayName("Test deleting a product that does not exists")
	@Test
	void testDeleteNonExistingProduct() {
		fail("Not yet implemented");
	}

	@DisplayName("Test retrieving a product that exists")
	@Test
	void testGetExistingProduct() {
		fail("Not yet implemented");
	}

	@DisplayName("Test retrieving a product that does not exists")
	@Test
	void testGetNonExistingProduct() {
		fail("Not yet implemented");
	}

	
    static Stream<Arguments> testSaveNewProductProvider() {
        return Stream.of(
                Arguments.of(products.get(0)),
                Arguments.of(products.get(1)),
                Arguments.of(products.get(2))
        );
    }
    
    static Stream<Arguments> testUpdateProductProvider() {
    	Product equity2 = new Equity();
    	equity2.setDescription("Alphabet Corporation");
    	equity2.setId(2);
    	equity2.setSymbol("GOOG");

        return Stream.of(
                Arguments.of(equity2)
        );
    }
    
}
