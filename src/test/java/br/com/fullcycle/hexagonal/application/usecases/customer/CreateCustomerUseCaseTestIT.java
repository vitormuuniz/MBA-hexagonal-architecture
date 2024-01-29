package br.com.fullcycle.hexagonal.application.usecases.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class CreateCustomerUseCaseTestIT extends IntegrationTest {

    @Autowired
    private CreateCustomerUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        //when
        final var output = useCase.execute(createInput);

        //then
        assertNotNull(output.id());
        assertEquals(expectedCPF, output.cpf());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedErrorMessage = "Customer already exists";

        createCustomer(expectedCPF, expectedEmail, expectedName);

        var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com Email duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedErrorMessage = "Customer already exists";

        createCustomer("111.222.333-44", expectedEmail, expectedName);

        var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void createCustomer(String cpf, String email, String name) {
        customerRepository.create(Customer.newCustomer(name, cpf, email));
    }
}
