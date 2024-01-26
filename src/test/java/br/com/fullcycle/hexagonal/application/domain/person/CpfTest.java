package br.com.fullcycle.hexagonal.application.domain.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CpfTest {

    @Test
    @DisplayName("Deve instanciar um CPF")
    public void testShouldInstantiateAnCPF() {
        //given
        final var expectedCPF = "123.456.789-01";

        //when
        final var actualCpf = new Cpf(expectedCPF);

        //then
        assertEquals(expectedCPF, actualCpf.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF inválido")
    public void testShouldNotInstantiateAnInvalidCPF() {
        //given
        final var expectedError = "Invalid value for CPF";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Cpf("123456.789-01")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF nulo")
    public void testShouldNotInstantiateAnNullCPF() {
        //given
        final var expectedError = "Invalid value for CPF";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Cpf(null)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}