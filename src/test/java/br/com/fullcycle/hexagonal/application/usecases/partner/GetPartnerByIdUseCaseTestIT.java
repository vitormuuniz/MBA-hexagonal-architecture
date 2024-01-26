package br.com.fullcycle.hexagonal.application.usecases.partner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;

class GetPartnerByIdUseCaseTestIT extends IntegrationTest {

    @Autowired
    private GetPartnerByIdUseCase useCase;

    @Autowired
    private PartnerJpaRepository partnerRepository;

    @AfterEach
    void tearDown() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um partner por id")
    public void testGetById() {
        //given
        final var expectedCNPJ = "41536538000100";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = createPartner(expectedCNPJ, expectedEmail, expectedName);

        final var input = new GetPartnerByIdUseCase.Input(aPartner.getId().toString());

        //when
        final var output = useCase.execute(input).get();

        //then
        assertEquals(aPartner.getId().toString(), output.id());
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

    private PartnerEntity createPartner(String cnpj, String email, String name) {
        final var partner = new PartnerEntity();
        partner.setCnpj(cnpj);
        partner.setEmail(email);
        partner.setName(name);

        return partnerRepository.save(partner);
    }
}