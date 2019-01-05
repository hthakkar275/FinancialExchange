package org.hemant.thakkar.financialexchange.repository;

import org.hemant.thakkar.financialexchange.domain.Participant;
import org.springframework.stereotype.Service;

@Service("participantMemoryRepositoryImpl")
public class ParticipantMemoryRepositoryImpl implements ParticipantRepository {

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
		// TODO Auto-generated method stub
		return null;
	}

}
