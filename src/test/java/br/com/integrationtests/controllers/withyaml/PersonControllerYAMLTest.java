package br.com.integrationtests.controllers.withyaml;

import br.com.config.TestConfigs;
import br.com.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.integrationtests.dto.person.PersonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYAMLTest {

    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();
        person = new PersonDTO();
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var foundPerson = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                .as(PersonDTO.class, yamlMapper);

        person = foundPerson;

        assertNotNull(foundPerson.getId());
        assertEquals("Linus", foundPerson.getFirstName());
        assertEquals("Benedict Torvalds", foundPerson.getLastName());
        assertEquals("Helsinki - Finland", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());

        assertTrue(foundPerson.getId() > 0);
        assertTrue(foundPerson.getEnabled());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_WILLIAM)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var createdPerson = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(person, yamlMapper)
            .when()
                .put()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
            .extract()
                .body()
            .as(PersonDTO.class, yamlMapper);


        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var updatedPerson = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(person, yamlMapper)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
            .extract()
                .body()
            .as(PersonDTO.class, yamlMapper);

        person = updatedPerson;

        assertNotNull(updatedPerson.getId());
        assertEquals("Linus", updatedPerson.getFirstName());
        assertEquals("Benedict Torvalds", updatedPerson.getLastName());
        assertEquals("Helsinki - Finland", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());

        assertTrue(updatedPerson.getId() > 0);
        assertTrue(updatedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var foundPerson = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, yamlMapper);

        person = foundPerson;

        assertNotNull(foundPerson.getId());
        assertEquals("Linus", foundPerson.getFirstName());
        assertEquals("Benedict Torvalds", foundPerson.getLastName());
        assertEquals("Helsinki - Finland", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());

        assertTrue(foundPerson.getId() > 0);
        assertFalse(foundPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {
            given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
            .pathParam("id", person.getId())
                .when()
            .delete("{id}")
                .then()
            .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO[].class, yamlMapper);

        List<PersonDTO> people = Arrays.asList(content);
        PersonDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertEquals("Vivyanne", personOne.getFirstName());
        assertEquals("Blaasch", personOne.getLastName());
        assertEquals("Apt 1871", personOne.getAddress());
        assertEquals("Female", personOne.getGender());

        assertTrue(personOne.getId() > 0);
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(3);

        assertNotNull(personFour.getId());
        assertEquals("Virginie", personFour.getFirstName());
        assertEquals("Chatto", personFour.getLastName());
        assertEquals("8th Floor", personFour.getAddress());
        assertEquals("Female", personFour.getGender());

        assertTrue(personFour.getId() > 0);
        assertFalse(personFour.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}