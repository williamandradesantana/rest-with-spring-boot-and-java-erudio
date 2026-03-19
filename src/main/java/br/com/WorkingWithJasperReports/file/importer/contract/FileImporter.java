package br.com.WorkingWithJasperReports.file.importer.contract;

import br.com.WorkingWithJasperReports.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
