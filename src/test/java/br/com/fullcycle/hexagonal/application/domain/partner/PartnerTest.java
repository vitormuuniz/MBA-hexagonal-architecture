package br.com.fullcycle.hexagonal.application.domain.partner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class PartnerTest {

    @Test
    @DisplayName("Deve instanciar um parceiro")
    public void testInstantiatePartner() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        //when
        final var actualPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        //then
        assertNotNull(actualPartner.partnerId());
        assertEquals(expectedCNPJ, actualPartner.cnpj().value());
        assertEquals(expectedEmail, actualPartner.email().value());
        assertEquals(expectedName, actualPartner.name().value());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com CNPJ inválido")
    public void testShouldNotInstantiateAPartnerWithInvalidCNPJ() {
        //given
        final var expectedError = "Invalid value for CNPJ";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Partner.newPartner("John Doe", "41536.538/0001-00", "john.doe@gmail.com")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com nome inválido")
    public void testShouldNotInstantiateAPartnerWithInvalidEmail() {
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Partner.newPartner(null, "41.536.538/0001-00", "john.doe@gmail.com")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com email inválido")
    public void testShouldNotInstantiateAPartnerWithInvalidName() {
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}
