package datacollection.datacollection.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedByDTO {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private UUID institutionId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
