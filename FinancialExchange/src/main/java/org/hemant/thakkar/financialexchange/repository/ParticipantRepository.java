package org.hemant.thakkar.financialexchange.repository;

import org.hemant.thakkar.financialexchange.domain.Participant;

public interface ParticipantRepository {
	long saveParticipant(Participant participant);
	long deleteParticipant(long participantId);
	Participant getParticipant(long participantId);
}
