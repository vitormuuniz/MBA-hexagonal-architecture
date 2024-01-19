package br.com.fullcycle.hexagonal.infrastructure.grapghql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.EventDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

public class EventResolver {

    private final EventService eventService;
    private final PartnerService partnerService;
    private final CustomerService customerService;

    public EventResolver(EventService eventService, PartnerService partnerService, CustomerService customerService) {
        this.eventService = Objects.requireNonNull(eventService);
        this.partnerService = Objects.requireNonNull(partnerService);
        this.customerService = Objects.requireNonNull(customerService);
    }

    @MutationMapping
    public CreateEventUseCase.Output createEvent(@Argument EventDTO input) {
        final var partnerId = Objects.requireNonNull(input.getPartner(), "Partner is required").getId();
        final var useCase = new CreateEventUseCase(eventService, partnerService);
        return useCase.execute(new CreateEventUseCase.Input(input.getDate(), input.getName(), input.getTotalSpots(), partnerId));
    }
}
