package datacollection.datacollection.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "Institution")
@Table(name = "institutions")
@Setter
@Getter
public class Institution {
    // ID
    @Id
    @GeneratedValue
    private UUID id;

    // NAME
    @Column(name = "name", nullable = false)
    private String name;

    // DESCRIPTION
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    // EMAIL
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // PHONE
    @Column(name = "phone", nullable = true)
    private String phone;

    // IS ACTIVE
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // ADDRESS
    @Column(name = "address")
    private String address;

    // CATEGORY ID
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    // CREATED AT
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // UPDATED AT
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // PRE PERSIST
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // PRE UPDATE
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // USERS
    @OneToMany(mappedBy = "institution", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User> users;

    // CATEGORY
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    // PROJECTS
    @OneToMany(mappedBy = "institution", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Project> projects;
}
