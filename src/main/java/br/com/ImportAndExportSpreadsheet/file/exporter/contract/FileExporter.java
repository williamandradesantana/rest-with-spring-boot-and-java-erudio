package br.com.ImportAndExportSpreadsheet.file.exporter.contract;

import br.com.ImportAndExportSpreadsheet.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {
    Resource exportFile(List<PersonDTO> people) throws Exception;
}
