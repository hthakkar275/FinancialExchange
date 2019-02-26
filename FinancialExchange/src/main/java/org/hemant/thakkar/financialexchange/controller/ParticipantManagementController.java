package org.hemant.thakkar.financialexchange.controller;

import org.hemant.thakkar.financialexchange.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.domain.Broker;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.ParticipantEntry;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.service.ParticipantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParticipantManagementController {

	@Autowired
	@Qualifier("participantManagementServiceImpl")
	private ParticipantManagementService participantManagmentService;
	
	@PostMapping(value = "/participant/broker", produces = "application/json", consumes = "application/json")
	public APIDataResponse<Broker> addEquity(@RequestBody ParticipantEntry participantEntry)  {
		APIDataResponse<Broker> response = new APIDataResponse<>();
		try {
			Broker participant = (Broker) participantManagmentService.addParticipant(participantEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.PARTICIPANT_ADDED.getMessage());
			response.setData(participant);
			response.setResponseCode(ResultCode.PARTICIPANT_ADDED.getCode());
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
