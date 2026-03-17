package br.com.UploadEDownloadDeArquivos.file.importer.contract;

import br.com.UploadEDownloadDeArquivos.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
