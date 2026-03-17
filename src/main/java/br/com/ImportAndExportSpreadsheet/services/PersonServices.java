package br.com.ImportAndExportSpreadsheet.services;

import br.com.ImportAndExportSpreadsheet.controllers.PersonController;
import br.com.ImportAndExportSpreadsheet.data.dto.PersonDTO;
import br.com.ImportAndExportSpreadsheet.exception.BadRequestException;
import br.com.ImportAndExportSpreadsheet.exception.RequiredObjectIsNullException;
import br.com.ImportAndExportSpreadsheet.exception.ResourceNotFoundException;
import br.com.ImportAndExportSpreadsheet.file.importer.contract.FileImporter;
import br.com.ImportAndExportSpreadsheet.file.importer.factory.FileImporterFactory;
import br.com.ImportAndExportSpreadsheet.model.Person;
import br.com.ImportAndExportSpreadsheet.repository.PersonRepository;
import br.com.UploadEDownloadDeArquivos.exception.FileStorageException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static br.com.Swagger.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;
    @Autowired
    private FileImporterFactory importer;
    @Autowired
    private PagedResourcesAssembler<PersonDTO> assembler;

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Find All People");

        var people = repository.findAll(pageable);
        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Find People by name!");

        var people = repository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, people);
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a valid file!");

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null!"));
            FileImporter importer = this.importer.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream)
                    .stream().map(dto ->
                            repository.save(parseObject(dto, Person.class))).toList();

            return entities.stream().map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Update one person!");

        var entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one person!");

        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        repository.disablePerson(id);

        var entity = repository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        repository.delete(entity);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map((person) -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
