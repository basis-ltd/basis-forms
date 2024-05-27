package datacollection.datacollection.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {

    private UUID id;
    private int sequence;
    private String name;
    private String label;
    private String placeholder;
    private boolean required;
    private String type;
    private String accept;
    private boolean disabled;
    private boolean readonly;
    private int maxLength;
    private int minLength;
    private int max;
    private int min;
    private String pattern;
    private boolean multiple;
    private String defaultValue;
    private boolean autocapitalize;
    private UUID sectionId;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FormSectionDTO section;
    private CreatedByDTO user;
}
