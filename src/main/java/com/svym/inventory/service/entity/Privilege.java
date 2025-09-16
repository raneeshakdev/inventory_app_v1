package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "privileges", schema = "public")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "privilege_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Privilege name is required")
    @Size(max = 100, message = "Privilege name must not exceed 100 characters")
    private String privilegeName;

    @Column(name = "description", length = 255)
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    // Default constructor
    public Privilege() {}

    // Constructor with parameters
    public Privilege(String privilegeName, String description) {
        this.privilegeName = privilegeName;
        this.description = description;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", privilegeName='" + privilegeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return id != null && id.equals(privilege.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
