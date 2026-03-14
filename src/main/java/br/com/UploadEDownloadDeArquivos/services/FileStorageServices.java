package br.com.UploadEDownloadDeArquivos.services;

import br.com.UploadEDownloadDeArquivos.config.FileStorageConfig;
import br.com.UploadEDownloadDeArquivos.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServices {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServices.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServices(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try {
            logger.info("Create directories");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create the directory where files will be stored");
            throw new FileStorageException("Could not create the directory where files will be stored", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename contains a invalid path sequence {}", fileName);
                throw new FileStorageException("Sorry! Filename contains a invalid path sequence " + fileName);
            }

            logger.info("Saving file in disk");
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file {}. Please try again!", fileName);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }
}
