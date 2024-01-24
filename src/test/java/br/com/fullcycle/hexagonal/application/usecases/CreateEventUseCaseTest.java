package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.application.entities.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;

        final var eventRepository = new InMemoryEventRepository();
        final var partnerRepository = new InMemoryPartnerRepository();

        final var aPartner = partnerRepository.create(Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com"));
        final var expectedPartnerId = aPartner.partnerId().value();

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var useCase = new CreateEventUseCase(eventRepository, partnerRepository);
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

        final var eventRepository = new InMemoryEventRepository();
        final var partnerRepository = new InMemoryPartnerRepository();

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var useCase = new CreateEventUseCase(eventRepository, partnerRepository);
        final var actualResponse = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualResponse.getMessage());
    }
}