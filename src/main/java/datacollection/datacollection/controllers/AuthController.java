package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.UserAuthDTO;
import datacollection.datacollection.repositories.UserRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.AuthResponse;
import datacollection.datacollection.utils.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // LOGIN
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody UserAuthDTO user) {
        try {
            UserAuthDTO existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser == null) {
                ApiResponse<Object> responseNotFound = new ApiResponse<>("User not found", null);
                return ResponseEntity.status(404).body(responseNotFound);
            }
            if (!PasswordEncoder.comparePasswords(user.getPassword(), existingUser.getPassword())) {
                ApiResponse<Object> responseUnauthorized = new ApiResponse<>("Email or password not correct", null);
                return ResponseEntity.status(400).body(responseUnauthorized);
            }
            AuthResponse<UserAuthDTO> responseOk = new AuthResponse<>("Authentication successful", "", existingUser);
            return ResponseEntity.status(200).body(responseOk);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }
}
