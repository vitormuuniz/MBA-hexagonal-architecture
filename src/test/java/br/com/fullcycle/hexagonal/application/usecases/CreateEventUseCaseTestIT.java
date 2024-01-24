package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.entities.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;

class CreateEventUseCaseTestIT extends IntegrationTest {

    @Autowired
    private CreateEventUseCase useCase;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;

        final var partner = createPartner("41536538000100", "john.doe@gmail.com", "John Doe");

        final var expectedPartnerId = partner.getId().toString();

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var actualResponse = useCase.execute(createInput);

        //then
        assertNotNull(actualResponse.id());
        assertEquals(expectedPartnerId, actualResponse.partnerId());
        assertEquals(expectedDate, actualResponse.date());
        assertEquals(expectedTotalSpots, actualResponse.totalSpots());
        assertEquals(expectedName, actualResponse.name());
    }

    @Test
    @DisplayName("Não deve criar um evento quando o partner não existir")
    public void testCreateEventWhenPartnerDoesNotExistsShouldThrowError() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = PartnerId.unique().value();
        final var expectedError = "Partner not found";

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var actualResponse = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualResponse.getMessage());
    }

    private Partner createPartner(String cnpj, String email, String name) {
        final var partner = new Partner();
        partner.setCnpj(cnpj);
        partner.setEmail(email);
        partner.setName(name);

        return partnerRepository.save(partner);
    }
}