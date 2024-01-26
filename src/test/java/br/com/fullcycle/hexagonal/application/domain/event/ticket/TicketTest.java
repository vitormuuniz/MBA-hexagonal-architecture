package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;

public class TicketTest {

    @Test
    @DisplayName("Deve instanciar um ticket")
    public void testInstantiateTicket() {
        //given
        final var expectedTicketStatus = TicketStatus.PENDING;

        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john-doe@gmail.com");

        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var expectedEventId = anEvent.eventId();

        final var aCustomer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var expectedCustomerId = aCustomer.customerId();

        //when
        final var actualTicket = Ticket.newTicket(aCustomer.customerId().value(), anEvent.eventId().value());

        //then
        assertNotNull(actualTicket.ticketId());
        assertNotNull(actualTicket.reservedAt());
        assertNull(actualTicket.paidAt());
        assertEquals(expectedCustomerId, actualTicket.customerId());
        assertEquals(expectedEventId, actualTicket.eventId());
        assertEquals(expectedTicketStatus, actualTicket.status());
    }
}