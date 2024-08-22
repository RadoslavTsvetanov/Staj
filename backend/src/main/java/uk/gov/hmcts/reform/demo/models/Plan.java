package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plan", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq_gen")
    @SequenceGenerator(name = "plan_seq_gen", sequenceName = "plan_seq", allocationSize = 1)
    private Long id;

    @Min(value = 0, message = "Estimated cost must be between 0 and 4")
    @Max(value = 4, message = "Estimated cost must be between 0 and 4")
    private Integer estCost;

    @NotNull(message = "Budget cannot be null")
    @Min(value = 0, message = "Budget must be between 0 and 4")
    @Max(value = 4, message = "Budget must be between 0 and 4")
    private Integer budget;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @OneToOne
    @JoinColumn(name = "date_window_id", referencedColumnName = "id")
    private DateWindow dateWindow;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "history_id", referencedColumnName = "id")
    private History history;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "plan_usernames", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "username")
    private Set<String> usernames = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "plan_locations",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstCost() {
        return estCost;
    }

    public void setEstCost(Integer estCost) {
        this.estCost = estCost;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public DateWindow getDateWindow() {
        return dateWindow;
    }

    public void setDateWindow(DateWindow dateWindow) {
        this.dateWindow = dateWindow;
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    public void addUsername(String username) {
        this.usernames.add(username);
    }

    // Method to remove a username from the plan
    public void removeUsername(String username) {
        this.usernames.remove(username);
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
}
