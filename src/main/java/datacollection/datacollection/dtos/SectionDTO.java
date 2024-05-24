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
public class SectionDTO {

    private UUID id;
    private String name;
    private String description;
    private int sequence;
    private UUID formId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FormDTO form;
}
