package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "preferences")
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preferences_seq_gen")
    @SequenceGenerator(name = "preferences_seq_gen", sequenceName = "preferences_seq", allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "preferences_interests", joinColumns = @JoinColumn(name = "preferences_id"))
    @Column(name = "interest")
    private List<String> interests;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
