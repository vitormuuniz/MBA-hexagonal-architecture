package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.Partner;

class GetPartnerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um partner por id")
    public void testGetById() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var expectedID = aPartner.partnerId().value().toString();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        //when
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);

        //then
        final var output = useCase.execute(input).get();

        assertEquals(expectedID, output.id());
        assertEquals(expectedCNPJ, output.cnpj());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um partner não existente por id")
    public void testGetByIdWithInvalidID() {
        //given
        final var expectedID = UUID.randomUUID().toString();

        final var partnerRepository = new InMemoryPartnerRepository();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        //when
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);

        final var output = useCase.execute(input);

        //then
        assertTrue(output.isEmpty());
    }
}