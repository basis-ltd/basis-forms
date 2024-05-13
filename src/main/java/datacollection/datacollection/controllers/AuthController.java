package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import datacollection.datacollection.repositories.UserRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.Security;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // LOGIN
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            System.out.println(user);
            UserDTO existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser == null) {
                ApiResponse<Object> responseNotFound = new ApiResponse<>("User not found", null);
                return ResponseEntity.status(404).body(responseNotFound);
            }
            if (!Security.comparePasswords(user.getPassword(), existingUser.getPassword())) {
                ApiResponse<Object> responseUnauthorized = new ApiResponse<>("Email or password not correct", null);
                return ResponseEntity.status(400).body(responseUnauthorized);
            }
            ApiResponse<UserDTO> responseOk = new ApiResponse<>("Authentication successful", existingUser);
            return ResponseEntity.status(200).body(responseOk);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }
}
