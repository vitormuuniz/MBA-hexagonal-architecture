package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import io.hypersistence.tsid.TSID;

class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = TSID.fast().toLong();


        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var eventService = mock(EventService.class);
        final var partnerService = mock(PartnerService.class);

        when(eventService.save(any())).thenAnswer(invocation -> {
            final var event = invocation.getArgument(0, Event.class);
            event.setId(TSID.fast().toLong());
            return event;
        });
        when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.of(new Partner()));

        final var useCase = new CreateEventUseCase(eventService, partnerService);
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
        final var expectedPartnerId = TSID.fast().toLong();
        final var expectedError = "Partner not found";


        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedTotalSpots, expectedPartnerId);

        //when
        final var eventService = mock(EventService.class);
        final var partnerService = mock(PartnerService.class);

        when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.empty());

        final var useCase = new CreateEventUseCase(eventService, partnerService);
        final var actualResponse = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualResponse.getMessage());
    }
}