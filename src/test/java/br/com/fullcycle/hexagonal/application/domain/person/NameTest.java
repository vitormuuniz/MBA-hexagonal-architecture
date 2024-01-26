package br.com.fullcycle.hexagonal.application.domain.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class NameTest {

    @Test
    @DisplayName("Deve instanciar um Name")
    public void testShouldInstantiateAnName() {
        //given
        final var expectedName = "John Doe";

        //when
        final var actualName = new Name(expectedName);

        //then
        assertEquals(expectedName, actualName.value());
    }

    @Test
    @DisplayName("Não deve instanciar um Name em branco")
    public void testShouldNotInstantiateAnBlankName() {
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Name("")
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um Name nulo")
    public void testShouldNotInstantiateAnNullName() {
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualException = assertThrows(
                ValidationException.class,
                () -> new Name(null)
        );

        //then
        assertEquals(expectedError, actualException.getMessage());
    }
}