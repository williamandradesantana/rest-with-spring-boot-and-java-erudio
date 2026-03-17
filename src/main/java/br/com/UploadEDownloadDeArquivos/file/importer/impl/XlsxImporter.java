package br.com.UploadEDownloadDeArquivos.file.importer.impl;

import br.com.UploadEDownloadDeArquivos.data.dto.PersonDTO;
import br.com.UploadEDownloadDeArquivos.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class XlsxImporter implements FileImporter {
    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }
}
