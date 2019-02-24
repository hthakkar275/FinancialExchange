package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.ParticipantEntry;

public interface ParticipantManagementService {
	Participant addParticipant(ParticipantEntry participantEntry) throws ExchangeException;
	Participant getParticipant(long participantId) throws ExchangeException;
	void deleteParticipant(long participantId) throws ExchangeException;
	Participant updateParticipant(long participantId, ParticipantEntry participantEntry) throws ExchangeException;
}
