package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;

public interface CustomerRepository {

    Optional<Customer> customerOfId(CustomerId customerId);
    Optional<Customer> customerOfCPF(Cpf cpf);
    Optional<Customer> customerOfEmail(Email email);
    Customer create(Customer customer);
    Customer update(Customer customer);
    void deleteAll();
}
