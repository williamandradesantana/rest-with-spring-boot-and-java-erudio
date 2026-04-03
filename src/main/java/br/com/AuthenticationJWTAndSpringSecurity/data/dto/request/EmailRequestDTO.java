package br.com.AuthenticationJWTAndSpringSecurity.data.dto.request;

import java.util.Objects;

public class EmailRequestDTO {

    private String to;
    private String subject;
    private String body;

    public EmailRequestDTO(){}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        EmailRequestDTO that = (EmailRequestDTO) o;
        return Objects.equals(to, that.to) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(to);
        result = 31 * result + Objects.hashCode(subject);
        result = 31 * result + Objects.hashCode(body);
        return result;
    }
}