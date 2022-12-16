package ru.gur.archdocumentgenerator.service;

import net.sf.jasperreports.engine.JRException;
import ru.gur.archdocumentgenerator.service.dto.CreateAgreementDto;

public interface DocumentGeneratorService {

    String generate(CreateAgreementDto createAgreementDto) throws JRException;

    String getUrl(String name);

    String delete(String name);
}
