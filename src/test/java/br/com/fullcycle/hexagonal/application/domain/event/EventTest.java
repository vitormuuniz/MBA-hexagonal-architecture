package br.com.fullcycle.hexagonal.application.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;

public class EventTest {

    @Test
    @DisplayName("Deve instanciar um evento")
    public void testInstantiateEvent() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedTickets = 0;
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");
        final var expectedPartnerId = aPartner.partnerId();

        //when
        final var actualEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);

        //then
        assertNotNull(actualEvent.eventId());
        assertEquals(expectedDate, actualEvent.date().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(expectedName, actualEvent.name().value());
        assertEquals(expectedTotalSpots, actualEvent.totalSpots());
        assertEquals(expectedTickets, actualEvent.allTickets().size());
        assertEquals(expectedPartnerId, actualEvent.partnerId());
    }

    @Test
    @DisplayName("Não deve instanciar um evento com data inválida")
    public void testShouldNotInstantiateAnEventWithInvalidDate() {
        //given
        final var expectedError = "Invalid date for Event";
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Event.newEvent("Disney on Ice", "111-222-3333", 10, aPartner)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um evento com nome inválido")
    public void testShouldNotInstantiateAnEventWithInvalidName() {
        //given
        final var expectedError = "Invalid value for Name";
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Event.newEvent(null, "2021-01-01", 10, aPartner)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um evento com total de lugares inválido")
    public void testShouldNotInstantiateAnEventWithInvalidTotalSpots() {
        //given
        final var expectedError = "Invalid totalSpots for Event";
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Event.newEvent("Disney on Ice", "2021-01-01", null, aPartner)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Deve reservar um ticket")
    public void testReserveTicket() {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedTickets = 1;
        final var expectedTicketOrder = 1;
        final var expectedTicketStatus = TicketStatus.PENDING;

        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");
        final var expectedPartnerId = aPartner.partnerId();

        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var expectedCustomerId = aCustomer.customerId();

        final var anEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);
        final var expectedEventId = anEvent.eventId();

        //when
        final var actualTicket = anEvent.reserveTicket(aCustomer.customerId());

        //then
        assertNotNull(actualTicket.ticketId());
        assertNotNull(actualTicket.reservedAt());
        assertNull(actualTicket.paidAt());
        assertEquals(expectedCustomerId, actualTicket.customerId());
        assertEquals(expectedEventId, actualTicket.eventId());
        assertEquals(expectedTicketStatus, actualTicket.status());

        assertEquals(expectedDate, anEvent.date().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(expectedName, anEvent.name().value());
        assertEquals(expectedTotalSpots, anEvent.totalSpots());
        assertEquals(expectedPartnerId, anEvent.partnerId());
        assertEquals(expectedTickets, anEvent.allTickets().size());

        final var actualEventTicket = anEvent.allTickets().iterator().next();
        assertEquals(actualTicket.ticketId(), actualEventTicket.ticketId());
        assertEquals(expectedTicketOrder, actualEventTicket.ordering());
        assertEquals(expectedCustomerId, actualEventTicket.customerId());
        assertEquals(expectedEventId, actualEventTicket.eventId());
    }

    @Test
    @DisplayName("Não deve reservar um ticket quando o evento está esgotado")
    public void testShouldNotReserveATicketWhenEventIsSoldOut() {
        //given
        final var expectedError = "Event sold out";

        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 1, aPartner);
        final var anotherCustomer = Customer.newCustomer("Vitor Doe", "123.456.789-10", "vitor.doe@gmail.com");

        anEvent.reserveTicket(anotherCustomer.customerId());

        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> anEvent.reserveTicket(aCustomer.customerId())
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve reservar mais de um ticket para o mesmo cliente")
    public void testShouldNotReserveMoreThanOneTicketToTheSameCustomer() {
        //given
        final var expectedError = "Email already registered";

        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        anEvent.reserveTicket(aCustomer.customerId());

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> anEvent.reserveTicket(aCustomer.customerId())
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}
