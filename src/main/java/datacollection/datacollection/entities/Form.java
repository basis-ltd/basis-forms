package datacollection.datacollection.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Form")
@Table(name = "forms")
@Setter
@Getter
public class Form {
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

    // IS ACTIVE
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // VISIBILITY
    @Column(name = "visibility", nullable = false)
    private String visibility = "public";

    // PROJECT ID
    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    // USER ID
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // CREATED AT
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // UPDATED AT
    @Column(name = "updated_at", nullable = false)
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

    // PROJECT
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    // USER
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
