package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.InstitutionDTO;
import datacollection.datacollection.dtos.ProjectDTO;
import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.Project;
import datacollection.datacollection.repositories.InstitutionRepository;
import datacollection.datacollection.repositories.ProjectRepository;
import datacollection.datacollection.repositories.UserRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.StringsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final StringsUtils stringsUtils = new StringsUtils();

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    // CREATE PROJECT
    @PostMapping(value = "")
    public ResponseEntity<Object> createProject(@RequestBody ProjectDTO project) {
        try {

            // CHECK IF INSTITUTION EXISTS
            InstitutionDTO institutionExists = institutionRepository.findInstitutionById(project.getInstitutionId());

            // CHECK IF PROJECT EXISTS
            ProjectDTO projectExists = projectRepository.findProjectByNameAndDescription(project.getName(), project.getDescription());

            if (projectExists != null) {
                ApiResponse<Object> projectAlreadyExists = new ApiResponse<>("Project already exists", stringsUtils.mapIdToObject(projectExists.getId()));
                return ResponseEntity.status(409).body(projectAlreadyExists);
            }

            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return ResponseEntity.status(404).body(institutionNotFound);
            }

            Project newProject = new Project();
            newProject.setName(project.getName());
            newProject.setDescription(project.getDescription());
            newProject.setStartDate(project.getStartDate());
            newProject.setEndDate(project.getEndDate());
            newProject.setInstitutionId(project.getInstitutionId());
            newProject.setUserId(project.getUserId());

            ApiResponse<Object> responseCreated = new ApiResponse<>("Project created successfully", projectRepository.save(newProject));

            return ResponseEntity.status(201).body(responseCreated);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseServerError);
        }
    }

    // FETCH PROJECTS
    @GetMapping(value = "")
    public ResponseEntity<Object> fetchProjects(@RequestParam(required = false, value = "institutionId") UUID institutionId, @RequestParam(value = "userId", required = false) UUID userId) {
        try {
            if (institutionId != null || userId != null) {
                ApiResponse<Object> responseFetched = new ApiResponse<>("Projects fetched successfully", projectRepository.findProjectsByInstitutionIdOrUserId(institutionId, userId));
                return ResponseEntity.status(200).body(responseFetched);
            }
            ApiResponse<Object> responseFetched = new ApiResponse<>("Projects fetched successfully", projectRepository.findAllProjects());
            return ResponseEntity.status(200).body(responseFetched);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseServerError);
        }
    }

    // GET PROJECT BY ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getProjectById(@PathVariable UUID id) {
        try {
            ProjectDTO projectExists = projectRepository.findProjectById(id);

            if (projectExists == null) {
                ApiResponse<Object> projectNotFound = new ApiResponse<>("Project not found", null);
                return ResponseEntity.status(404).body(projectNotFound);
            }

            ApiResponse<Object> responseFetched = new ApiResponse<>("Project fetched successfully", projectExists);
            return ResponseEntity.status(200).body(responseFetched);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseServerError);
        }
    }

    // UPDATE PROJECT
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable UUID id, @RequestBody ProjectDTO project) {
        try {
            ProjectDTO projectExists = projectRepository.findProjectById(id);

            if (projectExists == null) {
                ApiResponse<Object> projectNotFound = new ApiResponse<>("Project not found", null);
                return ResponseEntity.status(404).body(projectNotFound);
            }

            // CHECK IF INSTITUTION EXISTS
            InstitutionDTO institutionExists = institutionRepository.findInstitutionById(project.getInstitutionId());

            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return ResponseEntity.status(404).body(institutionNotFound);
            }

            // CHECK IF USER EXISTS
            UserDTO userExists = userRepository.findUserById(project.getUserId());

            // IF USER DOES NOT EXIST
            if (userExists == null) {
                ApiResponse<Object> userNotFound = new ApiResponse<>("User not found", null);
                return ResponseEntity.status(404).body(userNotFound);
            }

            ApiResponse<Object> responseUpdated = new ApiResponse<>("Project updated successfully", projectRepository.updateProject(project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate(), project.isActive(), project.getInstitutionId(), project.getUserId(), LocalDateTime.now(), id

            ));
            return ResponseEntity.status(200).body(responseUpdated);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseServerError);
        }

    }
    // DELETE PROJECT
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable UUID id) {
        try {
            ProjectDTO projectExists = projectRepository.findProjectById(id);

            if (projectExists == null) {
                ApiResponse<Object> projectNotFound = new ApiResponse<>("Project not found", null);
                return ResponseEntity.status(404).body(projectNotFound);
            }

            ApiResponse<Object> responseDeleted = new ApiResponse<>("Project deleted successfully", projectRepository.deleteProject(id));
            return ResponseEntity.status(200).body(responseDeleted);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseServerError);
        }
    }
}
