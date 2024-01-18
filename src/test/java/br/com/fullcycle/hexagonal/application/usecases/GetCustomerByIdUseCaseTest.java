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
    public void testGet() {
        //given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedId);
        aCustomer.setCpf(expectedCpf);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerService = mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerService);

        //then
        final var output = useCase.execute(input);

        assertEquals(expectedId, output.id());
        assertEquals(expectedCpf, output.cpf());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }
}