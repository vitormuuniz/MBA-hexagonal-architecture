package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Partner(PartnerId partnerId, Name name, Cnpj cnpj, Email email) {
    public Partner {
        if (partnerId == null) {
            throw new ValidationException("Invalid partnerId for Partner");
        }
    }

    public static Partner newPartner(String name, String cnpj, String email) {
        return new Partner(PartnerId.unique(), new Name(name), new Cnpj(cnpj), new Email(email));
    }
}
