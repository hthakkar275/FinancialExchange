package org.hemant.thakkar.financialexchange.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hemant.thakkar.financialexchange.domain.Broker;
import org.hemant.thakkar.financialexchange.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParticipantMemoryRepositoryImplTest {

	private static List<Participant> participants;
	
	static {
		participants = new ArrayList<>();
    	Participant p1 = new Broker();
    	p1.setName("Hemant Thakkar");
    	p1.setId(1);
    	participants.add(p1);
    	
    	Participant p2 = new Broker();
    	p2.setName("John Smith");
    	p2.setId(2);
    	participants.add(p2);

    	Participant p3 = new Broker();
    	p3.setName("Jane Doe");
    	p3.setId(3);
    	participants.add(p3);
	}
	
	private ParticipantMemoryRepositoryImpl participantRepository = new ParticipantMemoryRepositoryImpl();
		
	@DisplayName("Test saving a new participant")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testSaveNewParticipantProvider")
	void testSaveNewParticipant(Participant participant) {
		long participantId = participantRepository.saveParticipant(participant);
		Participant savedParticipant = participantRepository.getParticipant(participantId);
		assertEquals(participantId, savedParticipant.getId());
		assertEquals(participant.getName(), savedParticipant.getName());
	}

	@DisplayName("Test saving/updating existing participant")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndParticipantProvider")
	void testUpdateParticipant(Participant participant) {
		
		// Save the participant for the first time
		long participantId = participantRepository.saveParticipant(participant);
		
		// Now update the same participant, but with different symbol and description
		// The total count of participants should remain 1 but the update-saved
		// participant should have new symbol and description
		Participant updatedParticipant = new Broker();
		updatedParticipant.setId(participantId);
		updatedParticipant.setName("Hemant A Thakkar");
		long updatedParticipantId = participantRepository.saveParticipant(updatedParticipant);
		
		assertEquals(participantId, updatedParticipantId);
		assertEquals(1, participantRepository.getCount());

		Participant retrievedUpdatedParticipant  = participantRepository.getParticipant(participantId);
		assertEquals(participantId, retrievedUpdatedParticipant.getId());
		assertEquals(updatedParticipant.getName(), retrievedUpdatedParticipant.getName());
		assertNotEquals(participant.getName(), retrievedUpdatedParticipant.getName());
	}

	@DisplayName("Test deleting a participant that exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndParticipantProvider")
	void testDeleteExistingParticipant(Participant participant) {
		// Save the participant for the first time
		long participantId = participantRepository.saveParticipant(participant);
		assertNotEquals(participantId, 0);
		
		boolean deleted = participantRepository.deleteParticipant(participantId);
		assertTrue(deleted);
	}

	@DisplayName("Test deleting a participant that does not exists")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testUpdateAndParticipantProvider")
	void testDeleteNonExistingParticipant(Participant participant) {
		boolean deleted = participantRepository.deleteParticipant(participant.getId());
		assertFalse(deleted);
	}

	@DisplayName("Test retrieving a participant that does not exists")
	@Test
	void testGetNonExistingParticipant() {
		Participant participant = participantRepository.getParticipant(1000);
		assertNull(participant);
	}

	
    static Stream<Arguments> testSaveNewParticipantProvider() {
        return Stream.of(
                Arguments.of(participants.get(0)),
                Arguments.of(participants.get(1)),
                Arguments.of(participants.get(2))
        );
    }
    
    static Stream<Arguments> testUpdateAndParticipantProvider() {
    	Participant p2 = new Broker();
    	p2.setId(2);
    	p2.setName("Mike Smith");

        return Stream.of(
                Arguments.of(p2)
        );
    }

}
