package br.com.ImportAndExportSpreadsheet.file.exporter.impl;

import br.com.ImportAndExportSpreadsheet.file.exporter.contract.FileExporter;
import br.com.ImportAndExportSpreadsheet.model.Person;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExporter implements FileExporter {
    @Override
    public Resource exportFile(List<Person> people) throws Exception {
        return null;
    }
}
