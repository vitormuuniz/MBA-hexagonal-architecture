package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var partnerRepository = new InMemoryPartnerRepository();

        var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var useCase = new CreatePartnerUseCase(partnerRepository);

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

        final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, "another-email@gmail.com");

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        //when
        final var useCase = new CreatePartnerUseCase(partnerRepository);

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

        final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        var createInput = new CreatePartnerUseCase.Input("11.222.333/4444-55", expectedEmail, expectedName);

        //when
        final var useCase = new CreatePartnerUseCase(partnerRepository);

        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}