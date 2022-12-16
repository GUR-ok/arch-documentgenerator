package ru.gur.archdocumentgenerator.web.documentgenerator;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gur.archdocumentgenerator.service.DocumentGeneratorService;
import ru.gur.archdocumentgenerator.service.dto.CreateAgreementDto;
import ru.gur.archdocumentgenerator.web.documentgenerator.request.CreateAgreementRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final DocumentGeneratorService documentGeneratorService;

    @PostMapping("/reports")
    @Operation(summary = "Сгенерировать печатную форму")
    public String report(@Valid @RequestBody CreateAgreementRequest createAgreementRequest) {
        try {
            return documentGeneratorService.generate(CreateAgreementDto.builder()
                    .firstName(createAgreementRequest.getFirstName())
                    .agreementNumber(createAgreementRequest.getAgreementNumber())
                    .date(createAgreementRequest.getDate())
                    .product(createAgreementRequest.getProduct())
                    .passportNumber(createAgreementRequest.getPassportNumber())
                    .build());
        } catch (
                JRException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/reports")
    @Operation(summary = "Получить ссылку на документ")
    public String getUrl(@RequestParam String name) {
        return documentGeneratorService.getUrl(name);
    }

    @DeleteMapping("/reports")
    @Operation(summary = "Удалить документ")
    public String delete(@RequestParam String name) {
        return documentGeneratorService.delete(name);
    }
}
