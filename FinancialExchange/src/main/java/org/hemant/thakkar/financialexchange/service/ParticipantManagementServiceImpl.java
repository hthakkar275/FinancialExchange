package org.hemant.thakkar.financialexchange.service;

import org.hemant.thakkar.financialexchange.domain.Broker;
import org.hemant.thakkar.financialexchange.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.hemant.thakkar.financialexchange.domain.ParticipantEntry;
import org.hemant.thakkar.financialexchange.domain.ResultCode;
import org.hemant.thakkar.financialexchange.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("participantManagementServiceImpl")
public class ParticipantManagementServiceImpl implements ParticipantManagementService {

	@Autowired
	@Qualifier("participantMemoryRepositoryImpl")
	ParticipantRepository participantRepository;

	@Override
	public Participant addParticipant(ParticipantEntry participantEntry) throws ExchangeException {
		Participant participant = null;
		if (participantEntry.getParticipantType().equals("BROKER")) {
			participant = new Broker();
			participant.setName(participantEntry.getName());
			participantRepository.saveParticipant(participant);
		} else {
			throw new ExchangeException(ResultCode.UNSUPPORTED_ENTITY);
		}
		return participant;
	}

	@Override
	public void deleteParticipant(long participantId) throws ExchangeException {
		boolean deleted = participantRepository.deleteParticipant(participantId);
		if (!deleted) {
			Participant participant = participantRepository.getParticipant(participantId);
			if (participant == null) {
				throw new ExchangeException(ResultCode.PARTICIPANT_NOT_FOUND);
			}
		}
	}

	@Override
	public Participant updateParticipant(long participantId, ParticipantEntry participantEntry) throws ExchangeException {
		Participant participant = null;
		if (participantEntry.getParticipantType().equals("BROKER")) {
			participant = (Broker) participantRepository.getParticipant(participantId);
			if (participant == null) {
				throw new ExchangeException(ResultCode.PARTICIPANT_NOT_FOUND);
			}
			participant.setName(participantEntry.getName());
			participantRepository.saveParticipant(participant);
			participant = (Broker) participantRepository.getParticipant(participantId);
		} else {
			throw new ExchangeException(ResultCode.UNSUPPORTED_ENTITY);
		}
		return participant;
	}

	@Override
	public Participant getParticipant(long participantId) throws ExchangeException {
		Participant participant = participantRepository.getParticipant(participantId);
		if (participant == null) {
			throw new ExchangeException(ResultCode.PARTICIPANT_NOT_FOUND);
		}
		return participant;
	}

}
