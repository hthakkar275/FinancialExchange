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
class ProductMemoryRepositoryImplTest {

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
	@MethodSource("testUpdateAndProductProvider")
	void testUpdateProduct(Product product) {
		
		// Save the product for the first time
		long productId = productRepository.saveProduct(product);
		
		// Now update the same product, but with different symbol and description
		// The total count of products should remain 1 but the update-saved
		// product should have new symbol and description
		Product updatedProduct = new Equity();
		updatedProduct.setId(productId);
		updatedProduct.setSymbol("ALPH");
		updatedProduct.setDescription("Updated Alphabet, Inc.");
		long updatedProductId = productRepository.saveProduct(updatedProduct);
		
		assertEquals(productId, updatedProductId);
		assertEquals(1, productRepository.getCount());

		Product retrievedUpdatedProduct  = productRepository.getProduct(productId);
		assertEquals(productId, retrievedUpdatedProduct.getId());
		assertEquals(updatedProduct.getSymbol(), retrievedUpdatedProduct.getSymbol());
		assertEquals(updatedProduct.getDescription(), retrievedUpdatedProduct.getDescription());
		assertNotEquals(product.getSymbol(), retrievedUpdatedProduct.getSymbol());
		assertNotEquals(product.getDescription(), retrievedUpdatedProduct.getDescription());
	}

	@DisplayName("Test deleting a product that exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndProductProvider")
	void testDeleteExistingProduct(Product product) {
		// Save the product for the first time
		long productId = productRepository.saveProduct(product);
		assertNotEquals(productId, 0);
		
		boolean deleted = productRepository.deleteProduct(productId);
		assertTrue(deleted);
	}

	@DisplayName("Test deleting a product that does not exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndProductProvider")
	void testDeleteNonExistingProduct(Product product) {
		boolean deleted = productRepository.deleteProduct(product.getId());
		assertFalse(deleted);
	}

	@DisplayName("Test retrieving a product that does not exists")
	@Test
	void testGetNonExistingProduct() {
		Product product = productRepository.getProduct(1000);
		assertNull(product);
	}

	
    static Stream<Arguments> testSaveNewProductProvider() {
        return Stream.of(
                Arguments.of(products.get(0)),
                Arguments.of(products.get(1)),
                Arguments.of(products.get(2))
        );
    }
    
    static Stream<Arguments> testUpdateAndProductProvider() {
    	Product equity2 = new Equity();
    	equity2.setDescription("Alphabet Corporation");
    	equity2.setId(2);
    	equity2.setSymbol("GOOG");

        return Stream.of(
                Arguments.of(equity2)
        );
    }
    
}
