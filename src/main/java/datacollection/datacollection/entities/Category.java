package datacollection.datacollection.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "Category")
@Table(name = "categories")
@Setter
@Getter
public class Category {

    // ID
    @Id
    @GeneratedValue
    public UUID id;

    // NAME
    @Column(name = "name", nullable = false)
    private String name;

    // DESCRIPTION
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    // IS ACTIVE
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // CREATED AT
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // INSTITUTIONS
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Institution> institutions;

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
}
