package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

//Port
public class GetPartnerByIdUseCase extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

    private final PartnerService partnerService;

    public GetPartnerByIdUseCase(PartnerService partnerService) {
        this.partnerService = Objects.requireNonNull(partnerService);
    }

    @Override
    public Optional<Output> execute(Input input) {
        return partnerService.findById(input.id)
                .map(partner -> new Output(partner.getId(), partner.getCnpj(), partner.getEmail(), partner.getName()));
    }

    public record Input(Long id) {}
    public record Output(Long id, String cnpj, String email, String name) {}
}
