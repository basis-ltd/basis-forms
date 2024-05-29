package datacollection.datacollection.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Entry")
@Table(name = "entries")
@Getter
@Setter
public class Entry {

    // ID
    @Id
    @GeneratedValue
    private UUID id;

    // STATUS
    @Column(name = "status", nullable = false)
    private String status;

    // PROGRESS
    @Column(name = "progress")
    private int progress = 0;

    // FORM ID
    @Column(name = "form_id", nullable = false)
    private UUID formId;

    // USER ID
    @Column(name = "user_id")
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

    // FORM
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "form_id", insertable = false, updatable = false)
    private Form form;

    // USER
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
