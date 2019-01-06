package org.hemant.thakkar.financialexchange.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.domain.Broker;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.springframework.stereotype.Service;

@Service("participantMemoryRepositoryImpl")
public class ParticipantMemoryRepositoryImpl implements ParticipantRepository {

	Map<Long, Participant> participants;
	
	public ParticipantMemoryRepositoryImpl() {
		participants = new ConcurrentHashMap<Long, Participant>();
		createParticipants();
	}
	
	@Override
	public long saveParticipant(Participant participant) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteParticipant(long participantId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Participant getParticipant(long participantId) {
		return participants.get(participantId);
	}

	private void createParticipants() {
		Broker broker = new Broker();
		broker.setName("Hemant");
		participants.put(broker.getId(), broker);
	}
}
