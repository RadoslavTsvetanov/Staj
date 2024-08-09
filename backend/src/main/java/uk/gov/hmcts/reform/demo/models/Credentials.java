package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "credentials")
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_seq_gen")
    @SequenceGenerator(name = "credentials_seq_gen", sequenceName = "credentials_seq", allocationSize = 1)
    private Long id;

    private String password;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
