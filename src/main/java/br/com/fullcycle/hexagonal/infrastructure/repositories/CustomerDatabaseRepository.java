package br.com.fullcycle.hexagonal.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;

//Interface Adapter
//Adapta o que vem do domínio para o Driven/Secondary Actor
@Component
public class CustomerDatabaseRepository implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerDatabaseRepository(final CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = Objects.requireNonNull(customerJpaRepository);
    }
    @Override
    public Optional<Customer> customerOfId(final CustomerId customerId) {
        return this.customerJpaRepository.findById(UUID.fromString(customerId.value()))
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Optional<Customer> customerOfCPF(final Cpf cpf) {
        Objects.requireNonNull(cpf, "CPF cannot be null");
        return this.customerJpaRepository.findByCpf(cpf.value())
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Optional<Customer> customerOfEmail(final Email email) {
        Objects.requireNonNull(email, "Email cannot be null");
        return this.customerJpaRepository.findByEmail(email.value())
                .map(CustomerEntity::toCustomer);
    }

    @Override
    @Transactional
    public Customer create(final Customer customer) {
        return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
    }

    @Override
    public void deleteAll() {
        this.customerJpaRepository.deleteAll();
    }
}
