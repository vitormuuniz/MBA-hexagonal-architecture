package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;

class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedID = UUID.randomUUID().getMostSignificantBits();
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedID);
        aCustomer.setCpf(expectedCpf);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        final var input = new GetCustomerByIdUseCase.Input(expectedID);

        //when
        final var customerService = mock(CustomerService.class);
        when(customerService.findById(expectedID)).thenReturn(Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerService);

        //then
        final var output = useCase.execute(input).get();

        assertEquals(expectedID, output.id());
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
        final var customerService = mock(CustomerService.class);
        when(customerService.findById(expectedID)).thenReturn(Optional.empty());

        final var useCase = new GetCustomerByIdUseCase(customerService);

        //then
        final var output = useCase.execute(input);

        assertTrue(output.isEmpty());
    }
}