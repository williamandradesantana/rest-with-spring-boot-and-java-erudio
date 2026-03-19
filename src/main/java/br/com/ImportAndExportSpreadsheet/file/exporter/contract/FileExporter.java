package br.com.ImportAndExportSpreadsheet.file.exporter.contract;

import br.com.ImportAndExportSpreadsheet.model.Person;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {
    Resource exportFile(List<Person> people) throws Exception;
}
