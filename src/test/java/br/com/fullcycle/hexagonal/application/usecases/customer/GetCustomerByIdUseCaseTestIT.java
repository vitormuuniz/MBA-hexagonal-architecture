package br.com.fullcycle.hexagonal.application.usecases.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

class GetCustomerByIdUseCaseTestIT extends IntegrationTest {

    @Autowired
    private GetCustomerByIdUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = createCustomer(expectedCPF, expectedEmail, expectedName);
        final var expectedCustomerId = aCustomer.customerId().value();

        final var input = new GetCustomerByIdUseCase.Input(expectedCustomerId);

        //when
        final var output = useCase.execute(input).get();

        //then
        assertEquals(expectedCustomerId, output.id());
        assertEquals(expectedCPF, output.cpf());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalidID() {
        //given
        final var expectedID = UUID.randomUUID().toString();

        final var input = new GetCustomerByIdUseCase.Input(expectedID);

        //when
        final var output = useCase.execute(input);

        //then
        assertTrue(output.isEmpty());
    }

    private Customer createCustomer(String cpf, String email, String name) {
        return customerRepository.create(Customer.newCustomer(name, cpf, email));
    }
}