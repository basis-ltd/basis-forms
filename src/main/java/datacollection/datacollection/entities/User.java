package datacollection.datacollection.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // ID
    @Id
    @GeneratedValue
    private UUID id;

    // FIRST NAME
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // LAST NAME
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // EMAIL
    @Column(name = "email", nullable = false)
    private String email;

    // PASSWORD
    @Column(name = "password", nullable = false)
    private String password;

    // ROLE
    @Column(name = "role", nullable = false)
    private String role = "user";

    // PHONE
    @Column(name = "phone", nullable = true)
    private String phone;

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

    // INSTITUTION ID
    @Column(name = "institution_id", nullable = false)
    private UUID institutionId;

    // INSTITUTION
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institution_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Institution institution;

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
