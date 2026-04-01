package br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.services;

import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.controllers.BookController;
import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.data.dto.BookDTO;
import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.exception.RequiredObjectIsNullException;
import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.exception.ResourceNotFoundException;
import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.model.Book;
import br.com.AuthenticationJWTAndSpringSecurity.WorkingWithEmails.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static br.com.Swagger.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    @Autowired
    private BookRepository repository;
    @Autowired
    private PagedResourcesAssembler<BookDTO> assembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        var books = repository.findAll(pageable);
        var booksWithLinks = books.map((book) -> {
            var dto = parseObject(book, BookDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class)
                    .findAll(pageable.getPageNumber(),
                            pageable.getPageSize(),
                            String.valueOf(pageable.getSort())
                    )).withSelfRel();
        return assembler.toModel(booksWithLinks, findAllLinks);
    }

    public BookDTO findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        var dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        var entity = parseObject(book, Book.class);
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        var entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records not found this ID"));
        repository.delete(entity);
    }

    private static void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo((methodOn(BookController.class).create(dto))).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
