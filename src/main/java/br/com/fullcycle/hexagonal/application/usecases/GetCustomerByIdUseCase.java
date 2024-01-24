package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.entities.CustomerId;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

//Port
public class GetCustomerByIdUseCase extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
    }

    @Override
    public Optional<Output> execute(Input input) {
        return customerRepository.customerOfId(CustomerId.with(input.id))
                .map(customer -> new Output(
                                    customer.customerId().value(),
                                    customer.cpf().value(),
                                    customer.email().value(),
                                    customer.name().value())
                );
    }

    public record Input(String id) {}

    public record Output(String id, String cpf, String email, String name) {}
}
