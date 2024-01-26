package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryTicketRepository;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;

class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() {
        //given
        final var expectedTicketSize = 1;
        final var expectedTicketStatus = TicketStatus.PENDING.name();

        final var eventRepository = new InMemoryEventRepository();
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = eventRepository.create(Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner));
        final var eventId = anEvent.eventId().value();

        final var customerRepository = new InMemoryCustomerRepository();
        final var aCustomer = customerRepository.create(Customer.newCustomer("Vitor Doe", "123.456.789-01", "vitor.doe@gmail.com"));
        final var customerId = aCustomer.customerId().value();

        final var ticketRepository = new InMemoryTicketRepository();

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var useCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository, ticketRepository);
        final var actualResponse = useCase.execute(createInput);

        //then
        assertEquals(eventId, actualResponse.eventId());
        assertNotNull(actualResponse.ticketId());
        assertNotNull(actualResponse.reservationDate());
        assertEquals(expectedTicketStatus, actualResponse.ticketStatus());

        final var actualEvent = eventRepository.eventOfId(anEvent.eventId()).get();
        assertEquals(expectedTicketSize, actualEvent.allTickets().size());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWhenEventDoesNotExistsShouldThrowError() {
        //given
        final var expectedError = "Event not found";

        final var customerRepository = new InMemoryCustomerRepository();
        final var aCustomer = customerRepository.create(customerRepository.create(Customer.newCustomer("Vitor Doe", "123.456.789-01", "vitor.doe@gmail.com")));
        final var customerId = aCustomer.customerId().value();

        final var eventId = EventId.unique().value();

        final var ticketRepository = new InMemoryTicketRepository();
        final var eventRepository = new InMemoryEventRepository();

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var useCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository, ticketRepository);
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um cliente que não existe")
    public void testReserveTicketWhenCustomerDoesNotExistsShouldThrowError() {
        //given
        final var expectedError = "Customer not found";

        final var eventRepository = new InMemoryEventRepository();
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = eventRepository.create(eventRepository.create(Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner)));
        final var eventId = anEvent.eventId().value();

        final var customerId = CustomerId.unique().value();

        final var customerRepository = new InMemoryCustomerRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var useCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository, ticketRepository);
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar mais de um ticket de um cliente para o mesmo evento")
    public void testReserveTicketMoreThanOnceShouldThrowError() {
        //given
        final var expectedError = "Email already registered";

        final var customerRepository = new InMemoryCustomerRepository();
        final var aCustomer = customerRepository.create(Customer.newCustomer("Vitor Doe", "123.456.789-01", "vitor.doe@gmail.com"));
        final var customerId = aCustomer.customerId().value();

        final var eventRepository = new InMemoryEventRepository();
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = eventRepository.create(Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner));
        final var eventId = anEvent.eventId().value();

        final var ticketRepository = new InMemoryTicketRepository();
        ticketRepository.create(anEvent.reserveTicket(aCustomer.customerId()));

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var useCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository, ticketRepository);
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento esgotado")
    public void testReserveTicketWhenEventIsSoldOutShouldThrowError() {
        //given
        final var expectedError = "Event sold out";

        final var customerRepository = new InMemoryCustomerRepository();
        final var aCustomer = customerRepository.create(Customer.newCustomer("Vitor Doe", "123.456.789-01", "vitor.doe@gmail.com"));
        final var anotherCustomer = customerRepository.create(Customer.newCustomer("Pedro Doe", "123.456.789-10", "pedro.doe@gmail.com"));
        final var customerId = aCustomer.customerId().value();

        final var eventRepository = new InMemoryEventRepository();
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = eventRepository.create(Event.newEvent("Disney on Ice", "2021-01-01", 1, aPartner));
        final var eventId = anEvent.eventId().value();

        final var ticketRepository = new InMemoryTicketRepository();
        ticketRepository.create(anEvent.reserveTicket(anotherCustomer.customerId()));

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var useCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository, ticketRepository);
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}