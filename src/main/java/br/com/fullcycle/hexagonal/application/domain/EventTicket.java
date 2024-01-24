package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventTicket {

    private final TicketId ticketId;
    private final EventId eventId;
    private final CustomerId customerId;
    private Integer ordering;

    protected EventTicket(final TicketId ticketId, final EventId eventId, final CustomerId customerId, final Integer ordering) {
        if (ticketId == null) {
            throw new ValidationException("Invalid ticketId for EventTicket");
        }
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for EventTicket");
        }
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for EventTicket");
        }
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.setOrdering(ordering);
    }

    public TicketId ticketId() {
        return this.ticketId;
    }

    public EventId eventId() {
        return this.eventId;
    }

    public CustomerId customerId() {
        return this.customerId;
    }

    public Integer ordering() {
        return this.ordering;
    }
    private void setOrdering(final Integer ordering) {
        if (ordering == null) {
            throw new ValidationException("Invalid ordering for EventTicket");
        }
        this.ordering = ordering;
    }
}
