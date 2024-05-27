package datacollection.datacollection.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Field")
@Table(name = "fields")
public class Field {

    // ID
    @Id
    @GeneratedValue
    private UUID id;

    // SEQUENCE
    @Column(name = "sequence", nullable = false)
    private int sequence;

    // NAME
    @Column(name = "name")
    private String name;

    // LABEL
    @Column(name = "label", nullable = false)
    private String label;

    // PLACEHOLDER
    @Column(name = "placeholder")
    private String placeholder;

    // REQUIRED
    @Column(name = "required")
    private boolean required = false;

    // TYPE
    @Column(name = "type")
    private String type = "text";

    // ACCEPT
    @Column(name = "accept")
    private String accept = "*";

    // DISABLED
    @Column(name = "disabled")
    private boolean disabled = false;

    // READONLY
    @Column(name = "readonly")
    private boolean readonly = false;

    // MAX LENGTH
    @Column(name = "max_length")
    private int maxLength;

    // MIN LENGTH
    @Column(name = "min_length")
    private int minLength;

    // MAX
    @Column(name = "max")
    private int max;

    // MIN
    @Column(name = "min")
    private int min;

    // PATTERN
    @Column(name = "pattern")
    private String pattern;

    // MULTIPLE
    @Column(name = "multiple")
    private boolean multiple = false;

    // DEFAULT VALUE
    @Column(name = "default_value")
    private String defaultValue;

    // AUTOCAPITALIZE
    @Column(name = "autocapitalize")
    private boolean autocapitalize = false;

    // SECTION ID
    @Column(name = "section_id", nullable = false)
    private UUID sectionId;

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

    // SECTION
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "section_id", insertable = false, updatable = false)
    private Section section;

    // USER
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
