package br.com.fullcycle.hexagonal.application.domain;

import java.time.Instant;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;

public class Ticket {

    private final TicketId ticketId;
    private CustomerId customerId;
    private EventId eventId;
    private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;

    public Ticket(
            final TicketId ticketId,
            final String customerId,
            final String eventId,
            final TicketStatus status,
            final Instant paidAt,
            final Instant reservedAt
    ) {
        this.ticketId = ticketId;
        this.setCustomerId(customerId);
        this.setEventId(eventId);
        this.setStatus(status);
        this.setPaidAt(paidAt);
        this.setReservedAt(reservedAt);
    }

    public static Ticket newTicket(final String customerId, final String eventId) {
        return new Ticket(TicketId.unique(), customerId, eventId, TicketStatus.PENDING, null, Instant.now());
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public EventId eventId() {
        return eventId;
    }

    public TicketStatus status() {
        return status;
    }

    public Instant paidAt() {
        return paidAt;
    }

    public Instant reservedAt() {
        return reservedAt;
    }

    private void setCustomerId(final String customerId) {
        this.customerId = new CustomerId(customerId);
    }

    private void setEventId(final String eventId) {
        this.eventId = new EventId(eventId);
    }

    private void setStatus(final TicketStatus status) {
        if (status == null) {
            throw new ValidationException("Invalid status for Ticket");
        }
        this.status = status;
    }

    private void setPaidAt(final Instant paidAt) {
        this.paidAt = paidAt;
    }

    private void setReservedAt(final Instant reservedAt) {
        if (reservedAt == null) {
            throw new ValidationException("Invalid reservedAt for Ticket");
        }
        this.reservedAt = reservedAt;
    }
}