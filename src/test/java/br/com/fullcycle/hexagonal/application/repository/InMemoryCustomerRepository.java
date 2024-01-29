package br.com.fullcycle.hexagonal.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<Cpf, Customer> customersByCPF;
    private final Map<Email, Customer> customersByEmail;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCPF = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Customer> customerOfId(CustomerId customerId) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(customerId).value()));
    }

    @Override
    public Optional<Customer> customerOfCPF(Cpf cpf) {
        return Optional.ofNullable(this.customersByCPF.get(cpf));
    }

    @Override
    public Optional<Customer> customerOfEmail(Email email) {
        return Optional.ofNullable(this.customersByEmail.get(email));
    }

    @Override
    public Customer create(Customer customer) {
        this.customers.put(customer.customerId().value(), customer);
        this.customersByCPF.put(customer.cpf(), customer);
        this.customersByEmail.put(customer.email(), customer);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        this.customers.put(customer.customerId().value(), customer);
        this.customersByCPF.put(customer.cpf(), customer);
        this.customersByEmail.put(customer.email(), customer);
        return customer;
    }

    @Override
    public void deleteAll() {
        this.customers.clear();
        this.customersByCPF.clear();
        this.customersByEmail.clear();
    }
}
