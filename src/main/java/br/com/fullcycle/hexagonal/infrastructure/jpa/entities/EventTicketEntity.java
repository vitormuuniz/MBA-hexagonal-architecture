package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import java.util.Objects;
import java.util.UUID;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

    @Id
    private UUID ticketId;

    private UUID customerId;

    private int ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventEntity event;

    public EventTicketEntity() {
    }

    public EventTicketEntity(
            UUID ticketId,
            UUID customerId,
            Integer ordering,
            EventEntity event
    ) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.ordering = ordering;
        this.event = event;
    }

    public static EventTicketEntity of(final EventEntity event, final EventTicket eventTicket) {
        return new EventTicketEntity(
                UUID.fromString(eventTicket.ticketId().value()),
                UUID.fromString(eventTicket.customerId().value()),
                eventTicket.ordering(),
                event
        );
    }

    public EventTicket toTicket() {
        return new EventTicket(
                TicketId.with(this.ticketId.toString()),
                EventId.with(this.event.id().toString()),
                CustomerId.with(this.customerId.toString()),
                this.ordering
        );
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public UUID ticketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID customerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int ordering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public EventEntity event() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTicketEntity that = (EventTicketEntity) o;
        return ordering == that.ordering && Objects.equals(ticketId, that.ticketId) && Objects.equals(customerId, that.customerId) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, customerId, ordering, event);
    }
}
