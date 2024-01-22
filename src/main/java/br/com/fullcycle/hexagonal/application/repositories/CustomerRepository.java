package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.entities.Customer;
import br.com.fullcycle.hexagonal.application.entities.CustomerId;

public interface CustomerRepository {

    Optional<Customer> customerOfId(CustomerId customerId);
    Optional<Customer> customerOfCpf(String cpf);
    Optional<Customer> customerOfEmail(String email);
    Customer create(Customer customer);
    Customer update(Customer customer);
}
