package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Customer(CustomerId customerId, Name name, Cpf cpf, Email email) {
    public Customer {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for Customer");
        }
    }

    public static Customer newCustomer(String name, String cpf, String email) {
        return new Customer(CustomerId.unique(), new Name(name), new Cpf(cpf), new Email(email));
    }
}
