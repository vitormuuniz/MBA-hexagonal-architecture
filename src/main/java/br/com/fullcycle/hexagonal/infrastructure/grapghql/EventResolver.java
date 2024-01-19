package br.com.fullcycle.hexagonal.infrastructure.grapghql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;

@Controller
public class EventResolver {

    private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

    public EventResolver(final CreateEventUseCase createEventUseCase, final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase) {
        this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
        this.subscribeCustomerToEventUseCase = Objects.requireNonNull(subscribeCustomerToEventUseCase);
    }

    @MutationMapping
    public CreateEventUseCase.Output createEvent(@Argument NewEventDTO input) {
        return createEventUseCase.execute(new CreateEventUseCase.Input(input.date(), input.name(), input.totalSpots(), input.partnerId()));
    }

    @Transactional
    @MutationMapping
    public SubscribeCustomerToEventUseCase.Output subscribeCustomerToEvent(@Argument SubscribeDTO input) {
        return subscribeCustomerToEventUseCase.execute(new SubscribeCustomerToEventUseCase.Input(input.eventId(), input.customerId()));
    }
}
