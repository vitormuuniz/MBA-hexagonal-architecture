package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.application.entities.PartnerId;

public interface PartnerRepository {

    Optional<Partner> partnerOfId(PartnerId partnerId);
    Optional<Partner> partnerOfCNPJ(String cnpj);
    Optional<Partner> partnerOfEmail(String email);
    Partner create(Partner partner);
    Partner update(Partner partner);
}
