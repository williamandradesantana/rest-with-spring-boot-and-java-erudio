package br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.file.exporter.contract;

import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {
    Resource exportFile(List<PersonDTO> people) throws Exception;
}
