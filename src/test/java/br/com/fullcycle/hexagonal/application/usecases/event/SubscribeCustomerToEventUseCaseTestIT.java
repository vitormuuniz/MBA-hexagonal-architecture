package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.TicketJpaRepository;
import io.hypersistence.tsid.TSID;

class SubscribeCustomerToEventUseCaseTestIT extends IntegrationTest {
    
    @Autowired
    private SubscribeCustomerToEventUseCase useCase;
    
    @Autowired
    private CustomerJpaRepository customerRepository;
    
    @Autowired
    private EventJpaRepository eventRepository;

    @Autowired
    private TicketJpaRepository ticketRepository;
    
    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        customerRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    @Disabled
    public void testReserveTicket() {
        //given
        final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
        final var aEvent = createEvent("Disney on Ice", 10);

        final var createInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId().toString(), aCustomer.getId().toString());

        //when
        final var actualResponse = useCase.execute(createInput);

        //then
        assertEquals(aEvent.getId().toString(), actualResponse.eventId());
        assertNotNull(actualResponse.reservationDate());
        assertEquals(TicketStatus.PENDING.name(), actualResponse.ticketStatus());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWhenEventDoesNotExistsShouldThrowError() {
        //given
        final var expectedError = "Event not found";

        final var eventId = TSID.fast().toString();

        final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, aCustomer.getId().toString());

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

        final var eventId = TSID.fast().toString();
        final var customerId = TSID.fast().toString();

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

        final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
        final var aEvent = createEvent("Disney on Ice", 10);

        createTicket(aCustomer, aEvent);

        final var eventId = aEvent.getId().toString();
        final var customerId = aCustomer.getId().toString();

        final var createInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    private void createTicket(CustomerEntity customer, EventEntity event) {
        final var ticket = new TicketEntity();
        ticket.setCustomer(customer);
        ticket.setEvent(event);
        ticket.setStatus(TicketStatus.PENDING);

        ticketRepository.save(ticket);
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento esgotado")
    @Disabled
    public void testReserveTicketWhenEventIsSoldOutShouldThrowError() {
        //given
        final var expectedError = "Event sold out";

        final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
        final var aEvent = createEvent("Disney on Ice", 0);

        final var createInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId().toString(), aCustomer.getId().toString());

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    private CustomerEntity createCustomer(String cpf, String email, String name) {
        final var customer = new CustomerEntity();
        customer.setCpf(cpf);
        customer.setEmail(email);
        customer.setName(name);

        return customerRepository.save(customer);
    }

    private EventEntity createEvent(String name, Integer totalSpots) {
        final var event = new EventEntity();
        event.setName(name);
        event.setTotalSpots(totalSpots);

        return eventRepository.save(event);
    }
}