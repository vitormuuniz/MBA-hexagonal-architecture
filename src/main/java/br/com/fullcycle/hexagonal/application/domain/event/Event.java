package br.com.fullcycle.hexagonal.application.domain.event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Event {

    private final EventId eventId;
    private Name name;
    private LocalDate date;
    private Integer totalSpots;
    private PartnerId partnerId;
    private final Set<EventTicket> tickets;

    public Event(
            final EventId eventId,
            final String name,
            final String date,
            final Integer totalSpots,
            final PartnerId partnerId,
            final Set<EventTicket> tickets
    ) {
        this(eventId, tickets);
        this.setName(name);
        this.setDate(date);
        this.setTotalSpots(totalSpots);
        this.setPartnerId(partnerId);
    }

    private Event(final EventId eventId, final Set<EventTicket> tickets) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Event");
        }
        this.eventId = eventId;
        this.tickets = tickets != null ? tickets : new HashSet<>(0);
    }

    public static Event newEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return new Event(EventId.unique(), name, date, totalSpots, partner.partnerId(),null);
    }

    public static Event restore(
            String eventId,
            String name,
            String date,
            int totalSpots,
            String partnerId,
            Set<EventTicket> tickets
    ) {
        return new Event(EventId.with(eventId), name, date, totalSpots, PartnerId.with(partnerId), tickets);
    }

    public EventId eventId() {
        return this.eventId;
    }

    public Name name() {
        return this.name;
    }

    public LocalDate date() {
        return this.date;
    }

    public Integer totalSpots() {
        return this.totalSpots;
    }

    public PartnerId partnerId() {
        return this.partnerId;
    }

    public Set<EventTicket> allTickets() {
        return Collections.unmodifiableSet(this.tickets);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public Ticket reserveTicket(CustomerId customerId) {
        this.allTickets().stream()
                .filter(ticket -> Objects.equals(ticket.customerId(), customerId))
                .findFirst()
                .ifPresent(ticket -> {
                    throw new ValidationException("Email already registered");
                });

        if (this.totalSpots() < this.allTickets().size() + 1) {
            throw new ValidationException("Event sold out");
        }

        final var ticket = Ticket.newTicket(customerId, this.eventId());

        this.tickets.add(new EventTicket(ticket.ticketId(), this.eventId(), customerId, allTickets().size() + 1));

        return ticket;
    }

    private void setName(final String name) {
        this.name = new Name(name);
    }

    private void setDate(final String date) {
        if (date == null) {
            throw new ValidationException("Invalid date for Event");
        }
        try {
            this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (RuntimeException ex) {
            throw new ValidationException("Invalid date for Event", ex);
        }
    }

    private void setTotalSpots(final Integer totalSpots) {
        if (totalSpots == null) {
            throw new ValidationException("Invalid totalSpots for Event");
        }
        this.totalSpots = totalSpots;
    }

    private void setPartnerId(final PartnerId partnerId) {
        this.partnerId = partnerId;
    }
}
