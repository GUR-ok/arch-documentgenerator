package ru.gur.archdocumentgenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.gur.archdocumentgenerator.repository.MinioRepository;
import ru.gur.archdocumentgenerator.service.dto.CreateAgreementDto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DocumentGeneratorServiceImpl implements DocumentGeneratorService {

    private final MinioRepository minioRepository;

    public String generate(CreateAgreementDto createAgreementDto) throws JRException {
        InputStream jrxmlStream = DocumentGeneratorServiceImpl.class.getResourceAsStream("/Blank_A4.jrxml");
        JasperReport reporte = JasperCompileManager.compileReport(jrxmlStream);

        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(createAgreementDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JRDataSource jrDataSource = new JsonDataSource(inputStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, jrDataSource);

        final byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);
//        JasperExportManager.exportReportToPdfFile(jasperPrint, "invoice.pdf");
        final String name = createAgreementDto.getAgreementNumber() + "_" + RandomStringUtils.randomNumeric(5)
                + ".pdf";
        minioRepository.save(report, name);

        return name;
    }

    @Override
    public String getUrl(String name) {
        return minioRepository.getUrl(name);
    }

    @Override
    public String delete(String name) {
        minioRepository.remove(name);

        return name;
    }
}
