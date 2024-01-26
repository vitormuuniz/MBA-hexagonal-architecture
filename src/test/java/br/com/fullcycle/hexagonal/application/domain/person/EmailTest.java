package br.com.fullcycle.hexagonal.application.domain.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EmailTest {

    @Test
    @DisplayName("Deve instanciar um Email")
    public void testShouldInstantiateAnEmail() {
        //given
        final var expectedEmail = "john.doe@gmail.com";

        //when
        final var actualEmail = new Email(expectedEmail);

        //then
        assertEquals(expectedEmail, actualEmail.value());
    }

    @Test
    @DisplayName("Não deve instanciar um Email inválido")
    public void testShouldNotInstantiateAnInvalidEmail() {
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Email("john.doe@gmail")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um Email nulo")
    public void testShouldNotInstantiateAnNullEmail() {
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Email(null)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}