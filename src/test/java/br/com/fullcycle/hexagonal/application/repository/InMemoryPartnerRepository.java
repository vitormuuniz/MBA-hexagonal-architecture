package br.com.fullcycle.hexagonal.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partners;
    private final Map<Cnpj, Partner> partnersByCNPJ;
    private final Map<Email, Partner> partnersByEmail;

    public InMemoryPartnerRepository() {
        this.partners = new HashMap<>();
        this.partnersByCNPJ = new HashMap<>();
        this.partnersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Partner> partnerOfId(PartnerId customerId) {
        return Optional.ofNullable(this.partners.get(Objects.requireNonNull(customerId).value()));
    }

    @Override
    public Optional<Partner> partnerOfCNPJ(Cnpj cpf) {
        return Optional.ofNullable(this.partnersByCNPJ.get(cpf));
    }

    @Override
    public Optional<Partner> partnerOfEmail(Email email) {
        return Optional.ofNullable(this.partnersByEmail.get(email));
    }

    @Override
    public Partner create(Partner partner) {
        this.partners.put(partner.partnerId().value(), partner);
        this.partnersByCNPJ.put(partner.cnpj(), partner);
        this.partnersByEmail.put(partner.email(), partner);
        return partner;
    }

    @Override
    public Partner update(Partner partner) {
        this.partners.put(partner.partnerId().value(), partner);
        this.partnersByCNPJ.put(partner.cnpj(), partner);
        this.partnersByEmail.put(partner.email(), partner);
        return partner;
    }

    @Override
    public void deleteAll() {
        this.partners.clear();
        this.partnersByCNPJ.clear();
        this.partnersByEmail.clear();
    }
}
