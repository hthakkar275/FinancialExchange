package org.hemant.thakkar.financialexchange.controller;

import org.hemant.thakkar.financialexchange.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.domain.Equity;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.ProductEntry;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductManagementController {

	@Autowired
	@Qualifier("productManagementServiceImpl")
	private ProductManagementService productManagmentService;
	
	@PostMapping(value = "/product/equity", produces = "application/json", consumes = "application/json")
	public APIDataResponse<Equity> addEquity(@RequestBody ProductEntry productEntry)  {
		APIDataResponse<Equity> response = new APIDataResponse<>();
		try {
			Equity equity = (Equity) productManagmentService.addProduct(productEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.PRODUCT_ADDED.getMessage());
			response.setData(equity);
			response.setResponseCode(ResultCode.PRODUCT_ADDED.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	}
	
}
