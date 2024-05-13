package datacollection.datacollection.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "description"})
})
public class Project {
    // ID
    @Id
    @GeneratedValue
    private UUID id;

    // NAME
    @Column(name = "name", nullable = false)
    private String name;

    // DESCRIPTION
    @Column(name = "description", nullable = true)
    private String description;

    // START DATE
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    // END DATE
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate = LocalDateTime.now().plusYears(1);

    // INSTITUTION ID
    @Column(name = "institution_id", nullable = false)
    private UUID institutionId;

    // USER ID
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // IS ACTIVE
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

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

    // INSTITUTION
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institution_id", insertable = false, updatable = false)
    private Institution institution;

    // USER
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // FORMS
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Form> forms;
}