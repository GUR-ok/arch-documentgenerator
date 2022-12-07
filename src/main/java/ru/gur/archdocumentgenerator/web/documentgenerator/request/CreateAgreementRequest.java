package ru.gur.archdocumentgenerator.web.documentgenerator.request;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateAgreementRequest {

    @NotBlank
    private String agreementNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String passportNumber;

    @NotBlank
    private String product;

    @NotBlank
    private String date;
}
