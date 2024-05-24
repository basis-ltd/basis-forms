package datacollection.datacollection.dtos;

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
public class UserDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phone;
    private boolean isActive;
    private UUID institutionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InstitutionDTO institution;
}

