package org.hemant.thakkar.financialexchange.controller;

import org.hemant.thakkar.financialexchange.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderReport;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderManagementController {

	@Autowired
	@Qualifier("orderManagementServiceImpl")
	private OrderManagementService orderManagementService;
	
	@PostMapping(value = "/order", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderReport> acceptNewOrder(@RequestBody OrderEntry orderEntry) {
		APIDataResponse<OrderReport> response = new APIDataResponse<>();
		try {
			OrderReport orderReport = orderManagementService.acceptNewOrder(orderEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_ACCEPTED.getMessage());
			response.setData(orderReport);
			response.setResponseCode(ResultCode.ORDER_ACCEPTED.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 
	
	@GetMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderReport> getOrderStatus(@PathVariable("orderId") long orderId) {
		APIDataResponse<OrderReport> response = new APIDataResponse<>();
		try {
			OrderReport orderReport = orderManagementService.getOrderStatus(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_FOUND.getMessage());
			response.setData(orderReport);
			response.setResponseCode(ResultCode.ORDER_FOUND.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 

	@DeleteMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderReport> deleteOrder(@PathVariable("orderId") long orderId) {
		APIDataResponse<OrderReport> response = new APIDataResponse<>();
		try {
			OrderReport orderReport = orderManagementService.cancelOrder(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_CANCELLED.getMessage());
			response.setData(orderReport);
			response.setResponseCode(ResultCode.ORDER_CANCELLED.getCode());
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