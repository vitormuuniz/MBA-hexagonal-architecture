package br.com.fullcycle.hexagonal.application.usecases.partner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

class CreatePartnerUseCaseTestIT extends IntegrationTest {

    @Autowired
    private CreatePartnerUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var output = useCase.execute(createInput);

        //then
        assertNotNull(output.id());
        assertEquals(expectedCNPJ, output.cnpj());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedErrorMessage = "Partner already exists";

        var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        createPartner(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com Email duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedErrorMessage = "Partner already exists";

        createPartner("11.222.333/4444-55", expectedEmail, expectedName);

        var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void createPartner(String cnpj, String email, String name) {
        partnerRepository.create(Partner.newPartner(name, cnpj, email));
    }
}