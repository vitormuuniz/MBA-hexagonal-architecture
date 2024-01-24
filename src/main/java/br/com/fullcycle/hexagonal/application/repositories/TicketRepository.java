package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.Ticket;
import br.com.fullcycle.hexagonal.application.domain.TicketId;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId ticketId);
    Ticket create(Ticket ticket);
    Ticket update(Ticket ticket);
}
