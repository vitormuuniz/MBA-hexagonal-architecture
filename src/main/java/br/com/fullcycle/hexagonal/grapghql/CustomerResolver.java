package br.com.fullcycle.hexagonal.grapghql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;

//Adapter
@Controller
public class CustomerResolver {

    private final CustomerService customerService;

    public CustomerResolver(CustomerService customerService) {
        this.customerService = Objects.requireNonNull(customerService);
    }

    @MutationMapping
    public CreateCustomerUseCase.Output createCustomer(@Argument CustomerDTO input) {
        final var useCase = new CreateCustomerUseCase(customerService);
        return useCase.execute(new CreateCustomerUseCase.Input(input.getCpf(), input.getEmail(), input.getName()));
    }

    @QueryMapping
    public CustomerDTO customerOfId(@Argument Long id) {
        return customerService.findById(id)
                .map(CustomerDTO::new)
                .orElse(null);
    }
}
