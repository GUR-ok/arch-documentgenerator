package ru.gur.archdocumentgenerator.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateAgreementDto {

    String agreementNumber;

    String firstName;

    String passportNumber;

    String product;

    String date;
}
