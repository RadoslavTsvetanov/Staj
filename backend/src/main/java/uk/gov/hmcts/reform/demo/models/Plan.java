package uk.gov.hmcts.reform.demo.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq_gen")
    @SequenceGenerator(name = "plan_seq_gen", sequenceName = "plan_seq", allocationSize = 1)
    private Long id;

    private Integer estCost;
    private Integer budget;
    private String name;

    @OneToOne
    @JoinColumn(name = "date_window_id", referencedColumnName = "id")
    private DateWindow dateWindow;

    @ManyToMany
    @JoinTable(
        name = "plan_users",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "plan_locations",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();

    // Getters and setters
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
}
