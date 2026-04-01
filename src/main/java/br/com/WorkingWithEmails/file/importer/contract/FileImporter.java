package br.com.WorkingWithEmails.file.importer.contract;

import br.com.WorkingWithEmails.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
