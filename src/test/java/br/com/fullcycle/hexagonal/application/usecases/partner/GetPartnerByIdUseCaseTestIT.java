package br.com.fullcycle.hexagonal.application.usecases.partner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

class GetPartnerByIdUseCaseTestIT extends IntegrationTest {

    @Autowired
    private GetPartnerByIdUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um partner por id")
    public void testGetById() {
        //given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = createPartner(expectedCNPJ, expectedEmail, expectedName);
        final var expectedPartnerId = aPartner.partnerId().value();

        final var input = new GetPartnerByIdUseCase.Input(expectedPartnerId);

        //when
        final var output = useCase.execute(input).get();

        //then
        assertEquals(expectedPartnerId, output.id());
        assertEquals(expectedCNPJ, output.cnpj());
        assertEquals(expectedEmail, output.email());
        assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um partner não existente por id")
    public void testGetByIdWithInvalidID() {
        //given
        final var expectedID = UUID.randomUUID().toString();

        final var input = new GetPartnerByIdUseCase.Input(expectedID);

        //when
        final var output = useCase.execute(input);

        //then
        assertTrue(output.isEmpty());
    }

    private Partner createPartner(String cnpj, String email, String name) {
        return partnerRepository.create(Partner.newPartner(name, cnpj, email));
    }
}