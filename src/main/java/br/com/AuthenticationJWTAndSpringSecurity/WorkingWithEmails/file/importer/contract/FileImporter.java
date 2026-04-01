package br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.file.importer.contract;

import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
