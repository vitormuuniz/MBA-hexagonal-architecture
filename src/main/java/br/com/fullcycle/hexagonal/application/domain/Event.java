package br.com.fullcycle.hexagonal.application.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Event (EventId eventId, Name name, LocalDate date, Integer totalSpots, PartnerId partnerId) {
    public Event {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Event");
        }
        if (date == null) {
            throw new ValidationException("Invalid date for Event");
        }
        if (totalSpots == null || totalSpots <= 0) {
            throw new ValidationException("Invalid totalSpots for Event");
        }
        if (partnerId == null) {
            throw new ValidationException("Invalid partnerId for Event");
        }
    }

    public static Event newEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return new Event(EventId.unique(), new Name(name), LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE), totalSpots, partner.partnerId());
    }
}
