package org.hemant.thakkar.financialexchange.controller;

import org.hemant.thakkar.financialexchange.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.domain.APIResponse;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Order;
import org.hemant.thakkar.financialexchange.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.domain.OrderReport;
import org.hemant.thakkar.financialexchange.domain.OrderState;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.service.OrderBookService;
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
public class OrderBookManagementController {

	@Autowired
	@Qualifier("orderBookServiceImpl")
	private OrderBookService orderBookService;
	
	@PostMapping(value = "/orderBook/order", produces = "application/json", consumes = "application/json")
	public APIResponse acceptNewOrder(@RequestBody OrderEntry orderEntry) {
		APIResponse response = new APIResponse();
		try {
			orderBookService.addOrder(orderEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_ACCEPTED.getMessage());
			response.setResponseCode(ResultCode.ORDER_ACCEPTED.getCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 
	
	@GetMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderState> getOrderBook(@PathVariable("orderId") long orderId) {
		APIDataResponse<OrderState> response = new APIDataResponse<>();
		try {
			OrderState orderReport = orderBookService.getOrderStatus(orderId);
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
	public APIResponse deleteOrder(@PathVariable("orderId") long orderId) {
		APIResponse response = new APIResponse();
		try {
			orderBookService.cancelOrder(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_CANCELLED.getMessage());
			response.setResponseCode(ResultCode.ORDER_CANCELLED.getCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	} 

}
