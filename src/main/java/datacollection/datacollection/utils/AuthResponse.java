package datacollection.datacollection.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthResponse<UserDTO> {
    private String message;
    String token;
    private UserDTO user;
}
