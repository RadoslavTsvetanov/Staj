package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "credentials", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_seq_gen")
    @SequenceGenerator(name = "credentials_seq_gen", sequenceName = "credentials_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    public Credentials(String mail, String password123) {
    }

    public Credentials() {

    }

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
