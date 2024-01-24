package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Objects;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.Event;
import br.com.fullcycle.hexagonal.application.domain.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class CreateEventUseCase extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

    private final EventRepository eventRepository;

    private final PartnerRepository partnerRepository;

    public CreateEventUseCase(final EventRepository eventRepository, final PartnerRepository partnerRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    @Override
    public Output execute(final Input input) {
        final var partner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
                .orElseThrow(() -> new ValidationException("Partner not found"));

        var event = Event.newEvent(input.name, input.date, input.totalSpots, partner);

        event = eventRepository.create(event);

        return new Output(
                    event.eventId().value(),
                    event.date().toString(),
                    event.name().value(),
                    event.totalSpots(),
                    event.partnerId().value()
        );
    }

    public record Input(String date, String name, Integer totalSpots, String partnerId) {}

    public record Output(String id, String date, String name, Integer totalSpots, String partnerId) {}
}
