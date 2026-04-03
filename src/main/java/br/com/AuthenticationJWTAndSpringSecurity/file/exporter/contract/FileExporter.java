package br.com.AuthenticationJWTAndSpringSecurity.file.exporter.contract;

import br.com.AuthenticationJWTAndSpringSecurity.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {
    Resource exportFile(List<PersonDTO> people) throws Exception;
}
