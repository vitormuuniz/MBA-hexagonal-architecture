package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;

class GetCustomerByIdUseCaseTestIT extends IntegrationTest {

    @Autowired
    private GetCustomerByIdUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = createCustomer(expectedCpf, expectedEmail, expectedName);

        final var input = new GetCustomerByIdUseCase.Input(aCustomer.getId());

        //when
        final var output = useCase.execute(input).get();

        //then
        assertEquals(aCustomer.getId(), output.id());
        assertEquals(expectedCpf, output.cpf());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalidID() {
        //given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetCustomerByIdUseCase.Input(expectedID);

        //when
        final var output = useCase.execute(input);

        //then
        assertTrue(output.isEmpty());
    }

    private Customer createCustomer(String cpf, String email, String name) {
        final var customer = new Customer();
        customer.setCpf(cpf);
        customer.setEmail(email);
        customer.setName(name);

        return customerRepository.save(customer);
    }
}