package br.com.fullcycle.hexagonal.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.entities.Event;
import br.com.fullcycle.hexagonal.application.entities.EventId;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;

public class InMemoryEventRepository implements EventRepository {

    private final Map<String, Event> events;

    public InMemoryEventRepository() {
        this.events = new HashMap<>();
    }

    @Override
    public Optional<Event> eventOfId(EventId eventId) {
        return Optional.ofNullable(this.events.get(Objects.requireNonNull(eventId).value()));
    }

    @Override
    public Event create(Event event) {
        this.events.put(event.eventId().value(), event);
        return event;
    }

    @Override
    public Event update(Event event) {
        this.events.put(event.eventId().value(), event);
        return event;
    }
}
