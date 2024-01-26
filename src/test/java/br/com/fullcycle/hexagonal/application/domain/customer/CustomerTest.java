package br.com.fullcycle.hexagonal.application.domain.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CustomerTest {

    @Test
    @DisplayName("Deve instanciar um cliente")
    public void testInstantiateCustomer() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        //when
        final var actualCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        //then
        assertNotNull(actualCustomer.customerId());
        assertEquals(expectedCPF, actualCustomer.cpf().value());
        assertEquals(expectedEmail, actualCustomer.email().value());
        assertEquals(expectedName, actualCustomer.name().value());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com CPF inválido")
    public void testShouldNotInstantiateACustomerWithInvalidCPF() {
        //given
        final var expectedError = "Invalid value for CPF";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Customer.newCustomer("John Doe", "123456.789-01", "john.doe@gmail.com")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com nome inválido")
    public void testShouldNotInstantiateACustomerWithInvalidEmail() {
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Customer.newCustomer(null, "123.456.789-01", "john.doe@gmail.com")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com email inválido")
    public void testShouldNotInstantiateACustomerWithInvalidName() {
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}
