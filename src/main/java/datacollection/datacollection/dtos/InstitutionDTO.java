package datacollection.datacollection.dtos;

import datacollection.datacollection.entities.Institution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDTO {

    private UUID id;
    private String name;
    private String description;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private UUID categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
