package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;

public interface PartnerRepository {

    Optional<Partner> partnerOfId(PartnerId partnerId);
    Optional<Partner> partnerOfCNPJ(Cnpj cnpj);
    Optional<Partner> partnerOfEmail(Email email);
    Partner create(Partner partner);
    Partner update(Partner partner);
    void deleteAll();
}
