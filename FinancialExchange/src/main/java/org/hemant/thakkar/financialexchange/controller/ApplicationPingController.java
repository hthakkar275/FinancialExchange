package org.hemant.thakkar.financialexchange.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationPingController {

	@RequestMapping(value = "/status", method = RequestMethod.GET) 
	public String sayHello() {
		return "FiancialExchangeApplication is alive";
	}
}