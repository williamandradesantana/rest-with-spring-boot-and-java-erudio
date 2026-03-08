package br.com.integrationtests.dto.wrappers.xml;

import br.com.integrationtests.dto.person.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelPerson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    private List<PersonDTO> content;

    public PagedModelPerson(){}

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}
