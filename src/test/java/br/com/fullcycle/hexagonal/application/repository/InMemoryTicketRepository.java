package br.com.fullcycle.hexagonal.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;

public class InMemoryTicketRepository implements TicketRepository {

    private final Map<String, Ticket> tickets;
    public InMemoryTicketRepository() {
        this.tickets = new HashMap<>();
    }

    @Override
    public Optional<Ticket> ticketOfId(TicketId ticketId) {
        return Optional.ofNullable(this.tickets.get(Objects.requireNonNull(ticketId).value()));
    }

    @Override
    public Ticket create(Ticket ticket) {
        this.tickets.put(ticket.ticketId().value(), ticket);
        return ticket;
    }

    @Override
    public Ticket update(Ticket ticket) {
        this.tickets.put(ticket.ticketId().value(), ticket);
        return ticket;
    }

    @Override
    public void deleteAll() {
        this.tickets.clear();
    }
}
