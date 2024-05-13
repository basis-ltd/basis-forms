package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import datacollection.datacollection.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static datacollection.datacollection.utils.Security.hashPassword;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE USER
    @PostMapping(value = "")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            // CHECK IF EMAIL EXISTS
            UserDTO existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                return status(409).body(existingUser.getId());
            }
            User newUser = new User();
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmail(user.getEmail());
            newUser.setRole(user.getRole());
            newUser.setPhone(user.getPhone());
            newUser.setInstitutionId(user.getInstitutionId());
            newUser.setPassword(hashPassword(user.getPassword()));
            return status(201).body(userRepository.save(newUser));
        } catch (Exception e) {
            return status(500).body(e.getMessage());
        }
    }

    // UPDATE USER
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable UUID id) {
        try {
            // CHECK IF USER EXISTS
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                return status(404).body(null);
            }
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setRole(user.getRole());
            existingUser.setPhone(user.getPhone());
            return status(200).body(userRepository.save(existingUser));
        } catch (Exception e) {
            return status(500).body(e.getMessage());
        }
    }

    // FETCH ALL USERS
    @GetMapping(value = "")
    public ResponseEntity<Object> getUsers(@RequestParam(
            value = "institutionId",
            required = false
    ) UUID institutionId, @RequestParam(
            value = "role",
            required = false) String role) {
        if (institutionId != null) {
            return status(200).body(userRepository.findByInstitutionId(institutionId));
        } if (role != null) {
            return status(200).body(userRepository.findByRole(role));
        }
        return status(200).body(userRepository.findAll());
    }

    // DELETE USER
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        try {
            // CHECK IF USER EXISTS
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                return status(404).body(null);
            }
            userRepository.deleteById(id);
            return status(204).body(null);
        } catch (Exception e) {
            return status(500).body(e.getMessage());
        }
    }

    // GET USER BY ID
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Object> getUserById(@PathVariable("id") UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return status(404).body(null);
        }
        return ResponseEntity.ok(user);
    }
}
