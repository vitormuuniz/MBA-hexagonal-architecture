package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Objects;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

//Port
public class CreatePartnerUseCase extends UseCase<CreatePartnerUseCase.Input, CreatePartnerUseCase.Output> {

    private final PartnerRepository partnerRepository;

    public CreatePartnerUseCase(PartnerRepository partnerRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    @Override
    public Output execute(Input input) {
        if (partnerRepository.partnerOfCNPJ(input.cnpj).isPresent()) {
            throw new ValidationException("Partner already exists");
        }
        if (partnerRepository.partnerOfEmail(input.email).isPresent()) {
            throw new ValidationException("Partner already exists");
        }

        var partner = partnerRepository.create(Partner.newPartner(input.name, input.cnpj, input.email));

        return new Output(
                partner.partnerId().value().toString(),
                partner.cnpj().value(),
                partner.email().value(),
                partner.name().value()
        );
    }

    public record Input(String cnpj, String email, String name) {}
    public record Output(String id, String cnpj, String email, String name) {}
}
