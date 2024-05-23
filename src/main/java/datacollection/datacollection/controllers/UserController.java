package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.InstitutionDTO;
import datacollection.datacollection.dtos.UserAuthDTO;
import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import datacollection.datacollection.repositories.InstitutionRepository;
import datacollection.datacollection.repositories.UserRepository;
import datacollection.datacollection.services.MailService;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.StringsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static datacollection.datacollection.utils.PasswordEncoder.hashPassword;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final StringsUtils stringsUtils = new StringsUtils();
    @Autowired
    private MailService mailService;

    public UserController(UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    // CREATE USER
    @PostMapping(value = "")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            // CHECK IF EMAIL EXISTS
            UserAuthDTO existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                ApiResponse<Object> userConflict = new ApiResponse<>("User already exists", existingUser.getId());
                return status(409).body(userConflict);
            }

            // GENERATE PASSWORD
            String password = stringsUtils.generateRandomString(8);

            // CHECK IF INSTITUTION EXISTS
            InstitutionDTO institutionExists = institutionRepository.findInstitutionById(user.getInstitutionId());

            // IF INSTITUTION DOES NOT EXIST
            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return status(404).body(institutionNotFound);
            }

            User newUser = new User();
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEmail(user.getEmail());
            newUser.setRole(user.getRole());
            newUser.setPhone(user.getPhone());
            newUser.setInstitutionId(user.getInstitutionId());
            newUser.setPassword(hashPassword(password));

            ApiResponse<Object> response = new ApiResponse<>("User created successfully", userRepository.save(newUser));
            // SEND EMAIL TO USER
            mailService.sendMail(
                    newUser.getEmail(),
                    "Account Created",
                    "Your account has been created successfully. \n\nAccess your account using the following password: " + password
            );
            return status(201).body(response);
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
            ApiResponse<Object> usersList = new ApiResponse<>("Users retrieved successfully", userRepository.findByInstitutionId(institutionId));
            return status(200).body(usersList);
        }
        if (role != null) {
            ApiResponse<Object> usersList = new ApiResponse<>("Users retrieved successfully", userRepository.findByRole(role));
            return status(200).body(usersList);
        }
        ApiResponse<Object> usersList = new ApiResponse<>("Users retrieved successfully", userRepository.findAll());
        return status(200).body(usersList);
    }

    // DELETE USER
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        try {
            // CHECK IF USER EXISTS
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                ApiResponse<Object> response = new ApiResponse<>("User not found", null);
                return status(404).body(response);
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
        UserDTO user = userRepository.findUserById(id);
        if (user == null) {
            ApiResponse<Object> userNotFound = new ApiResponse<>("User not found", null);
            return status(404).body(userNotFound);
        }
        ApiResponse<Object> userFound = new ApiResponse<>("User found", user);
        return ResponseEntity.ok(userFound);
    }
}
