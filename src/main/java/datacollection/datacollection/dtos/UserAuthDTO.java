package datacollection.datacollection.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class UserAuthDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private boolean isActive;
    private UUID institutionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InstitutionDTO institution;
}
