package datacollection.datacollection.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FormDTO {

    private UUID id;
    private String name;
    private String description;
    private String visibility;
    private boolean isActive;
    private UUID projectId;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProjectDTO project;
    private CreatedByDTO user;
}
