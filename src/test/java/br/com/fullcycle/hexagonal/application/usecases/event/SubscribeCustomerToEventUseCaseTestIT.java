package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;

class SubscribeCustomerToEventUseCaseTestIT extends IntegrationTest {
    
    @Autowired
    private SubscribeCustomerToEventUseCase useCase;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PartnerRepository partnerRepository;
    
    @BeforeEach
    void tearDown() {
        eventRepository.deleteAll();
        customerRepository.deleteAll();
        ticketRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() {
        //given
        final var aPartner = createPartner("41.536.538/0001-00", "john.doe@gmail.com", "John Doe");
        final var anEvent = createEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var expectedEventId = anEvent.eventId().value();

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var createInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, aCustomer.customerId().value());

        //when
        final var actualResponse = useCase.execute(createInput);

        //then
        assertEquals(expectedEventId, actualResponse.eventId());
        assertNotNull(actualResponse.reservationDate());
        assertEquals(TicketStatus.PENDING.name(), actualResponse.ticketStatus());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWhenEventDoesNotExistsShouldThrowError() {
        //given
        final var expectedError = "Event not found";

        final var eventId = EventId.unique().value();

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, aCustomer.customerId().value());

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um cliente que não existe")
    public void testReserveTicketWhenCustomerDoesNotExistsShouldThrowError() {
        //given
        final var expectedError = "Customer not found";

        final var eventId = EventId.unique().value();
        final var customerId = CustomerId.unique().value();

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar mais de um ticket de um cliente para o mesmo evento")
    public void testReserveTicketMoreThanOnceShouldThrowError() {
        //given
        final var expectedError = "Email already registered";

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");
        final var customerId = aCustomer.customerId().value();

        final var aPartner = createPartner("41.536.538/0001-00", "john.doe@gmail.com", "John Doe");
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
        final var eventId = anEvent.eventId().value();

        createTicket(anEvent.reserveTicket(aCustomer.customerId()));

        eventRepository.create(anEvent);

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento esgotado")
    public void testReserveTicketWhenEventIsSoldOutShouldThrowError() {
        //given
        final var expectedError = "Event sold out";

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");
        final var anotherCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");
        final var aPartner = createPartner("41.536.538/0001-00", "john.doe@gmail.com", "John Doe");
        final var anEvent = Event.newEvent("Disney on Ice", "2021-01-01", 1, aPartner);
        final var eventId = anEvent.eventId().value();

        anEvent.reserveTicket(anotherCustomer.customerId());

        eventRepository.create(anEvent);

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, aCustomer.customerId().value());

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    private Customer createCustomer(String cpf, String email, String name) {
        return customerRepository.create(Customer.newCustomer(name, cpf, email));
    }

    private Event createEvent(String name, String date, Integer totalSpots, Partner partner) {
        return eventRepository.create(Event.newEvent(name, date, totalSpots, partner));
    }

    private void createTicket(Ticket ticket) {
        ticketRepository.create(ticket);
    }

    private Partner createPartner(String cnpj, String email, String name) {
        return partnerRepository.create(Partner.newPartner(name, cnpj, email));
    }
}