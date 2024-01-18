package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.services.CustomerService;

public class GetCustomerByIdUseCase extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

    private final CustomerService customerService;

    public GetCustomerByIdUseCase(CustomerService customerService) {
        this.customerService = Objects.requireNonNull(customerService);
    }

    @Override
    public Optional<Output> execute(Input input) {
        var customer = customerService.findById(input.id);
        if (customer.isEmpty()) {
            return new ValidationException("Customer not found");
        }

        return Optional.of(new Output(customer.get().getId(), customer.get().getCpf(), customer.get().getEmail(), customer.get().getName()));
    }

    public record Input(Long id) {}
    public record Output(Long id, String cpf, String email, String name) {}
}
