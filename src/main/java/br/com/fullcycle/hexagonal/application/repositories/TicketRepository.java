package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId ticketId);
    Ticket create(Ticket ticket);
    Ticket update(Ticket ticket);
    void deleteAll();
}
