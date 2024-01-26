package br.com.fullcycle.hexagonal.application.domain.person;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CnpjTest {

    @Test
    @DisplayName("Deve instanciar um CNPJ")
    public void testShouldInstantiateAnCNPJ() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";

        //when
        final var actualCnpj = new Cnpj(expectedCNPJ);

        //then
        assertEquals(expectedCNPJ, actualCnpj.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ inválido")
    public void testShouldNotInstantiateAnInvalidCNPJ() {
        //given
        final var expectedError = "Invalid value for CNPJ";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Cnpj("41536.538/0001-00")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ nulo")
    public void testShouldNotInstantiateAnNullCNPJ() {
        //given
        final var expectedError = "Invalid value for CNPJ";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Cnpj(null)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}