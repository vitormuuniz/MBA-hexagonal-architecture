package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.Partner;
import br.com.fullcycle.hexagonal.application.domain.PartnerId;

public interface PartnerRepository {

    Optional<Partner> partnerOfId(PartnerId partnerId);
    Optional<Partner> partnerOfCNPJ(String cnpj);
    Optional<Partner> partnerOfEmail(String email);
    Partner create(Partner partner);
    Partner update(Partner partner);
}
