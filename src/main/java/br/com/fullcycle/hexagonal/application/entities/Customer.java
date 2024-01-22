package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Customer(CustomerId customerId, String name, String cpf, String email) {

    public Customer {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for Customer");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Invalid name for Customer");
        }
        if (cpf == null || cpf.isBlank() || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")) {
            throw new ValidationException("Invalid cpf for Customer");
        }
        if (email == null || email.isBlank() || !email.matches("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")) {
            throw new ValidationException("Invalid email for Customer");
        }
    }

    public static Customer newCustomer(String name, String cpf, String email) {
        return new Customer(CustomerId.unique(), name, cpf, email);
    }
}
