package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.FormDTO;
import datacollection.datacollection.dtos.FormSectionDTO;
import datacollection.datacollection.dtos.ProjectDTO;
import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.Form;
import datacollection.datacollection.entities.Section;
import datacollection.datacollection.repositories.FormRepository;
import datacollection.datacollection.repositories.ProjectRepository;
import datacollection.datacollection.repositories.SectionRepository;
import datacollection.datacollection.repositories.UserRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.StringsUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/forms")
@CrossOrigin(origins = "*")
public class FormController {

    // REPOSITORIES
    private final FormRepository formRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;

    // UTILS
    private final StringsUtils stringsUtils = new StringsUtils();

    public FormController(ProjectRepository projectRepository, FormRepository formRepository, UserRepository userRepository, SectionRepository sectionRepository) {
        this.projectRepository = projectRepository;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
    }

    // CREATE FORM
    @PostMapping(value = "")
    public ResponseEntity<Object> createForm(@RequestBody FormDTO form) {

        try {

            // CHECK IF ALL REQUIRED FIELDS ARE PROVIDED
            if (form.getName() == null || form.getProjectId() == null || form.getUserId() == null || form.getVisibility() == null) {
                ApiResponse<Object> missingFields = new ApiResponse<>("Name, parent project, user, and visibility are required", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(missingFields);
            }

            // CHECK IF PROJECT EXISTS
            ProjectDTO projectsExists = projectRepository.findProjectById(form.getProjectId());

            // IF PROJECT NOT FOUND
            if (projectsExists == null) {
                ApiResponse<Object> projectNotFound = new ApiResponse<>("Project not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectNotFound);
            }

            // CHECK IF USER EXISTS
            UserDTO userExists = userRepository.findUserById(form.getUserId());

            // IF USER DOES NOT EXIST
            if (userExists == null) {
                ApiResponse<Object> userNotFound = new ApiResponse<>("User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
            }

            // CHECK IF FORM ALREADY EXISTS
            FormDTO formExists = formRepository.findByNameAndProjectId(form.getName(), form.getProjectId());

            if (formExists != null) {
                ApiResponse<Object> formConflict = new ApiResponse<>("Form with this name already exists", stringsUtils.mapIdToObject(formExists.getId()));
                return ResponseEntity.status(HttpStatus.CONFLICT).body(formConflict);
            }

            // CREATE FORM
            Form newForm = new Form();
            newForm.setName(form.getName());
            newForm.setDescription(form.getDescription());
            newForm.setVisibility(form.getVisibility());
            newForm.setProjectId(form.getProjectId());
            newForm.setUserId(form.getUserId());
            form.setSectionsCount(0L);

            Form formCreated =  formRepository.save(newForm);
            // CREATE DEFAULT SECTION
            Section generalSection = new Section();
            generalSection.setName("general");
            generalSection.setDescription("default section");
            generalSection.setFormId(formCreated.getId());
            generalSection.setSequence(1);
            sectionRepository.save(generalSection);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>("Form created successfully", formCreated)
            );
        } catch (Exception e) {
            ApiResponse<Object> serverError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError);
        }
    }

    // FETCH FORMS
    @GetMapping(value = "")
    public ResponseEntity<Object> fetchForms(@RequestParam(required = false, value = "projectId") UUID projectId, @RequestParam(value = "userId", required = false) UUID userId, @RequestParam(value
            = "institutionId", required = false) UUID institutionId) {
        try {

            // FETCH ALL FORMS IN AN INSTITUTION
            if (institutionId != null) {
                ApiResponse<Object> responseFetched = new ApiResponse<>("Forms fetched successfully", formRepository.findFormsByInstitutionId(institutionId));
                return ResponseEntity.status(HttpStatus.OK).body(responseFetched);
            }

            if (projectId != null || userId != null) {
                // FIND PROJECT FORMS
                if (userId == null) {
                    ApiResponse<Object> responseFetched = new ApiResponse<>("Forms fetched successfully", formRepository.findFormsByProjectId(projectId));
                    return ResponseEntity.status(HttpStatus.OK).body(responseFetched);
                }
                // FIND USER FORMS
                if (projectId == null) {
                    ApiResponse<Object> responseFetched = new ApiResponse<>("Forms fetched successfully", formRepository.findFormsByUserId(userId));
                    return ResponseEntity.status(HttpStatus.OK).body(responseFetched);
                }
                // FIND USER AND PROJECT FORMS
                ApiResponse<Object> responseFetched = new ApiResponse<>("Forms fetched successfully", formRepository.findFormsByProjectIdAndUserId(projectId, userId));
                return ResponseEntity.status(HttpStatus.OK).body(responseFetched);
            }
            ApiResponse<Object> responseFetched = new ApiResponse<>("Forms fetched successfully", formRepository.findAllForms());
            return ResponseEntity.status(HttpStatus.OK).body(responseFetched);
        } catch (Exception e) {
            ApiResponse<Object> responseServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseServerError);
        }
    }

    // FIND FORM BY ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findFormById(@PathVariable UUID id) {
        try {

            // FETCH FORMS
            FormDTO formExists = formRepository.findFormById(id);

            if (formExists == null) {
                ApiResponse<Object> formNotFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formNotFound);
            }

            // FETCH SECTIONS
            List<FormSectionDTO> formSections = sectionRepository.findByFormId(id);

            // ASSIGN SECTIONS TO FORM
            formExists.setSections(formSections);

            ApiResponse<Object> formFound = new ApiResponse<>("Form found", formExists);
            return ResponseEntity.status(HttpStatus.OK).body(formFound);
        } catch (Exception e) {
            ApiResponse<Object> serverError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError);
        }
    }

    // UPDATE FORM
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateForm(@PathVariable UUID id, @RequestBody FormDTO form) {
        try {
            FormDTO formExists = formRepository.findFormById(id);
            if (formExists == null) {
                ApiResponse<Object> formNotFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formNotFound);
            }

            // CHECK IF PROJECT EXISTS
            ProjectDTO projectsExists = projectRepository.findProjectById(form.getProjectId());

            // IF PROJECT NOT FOUND
            if (projectsExists == null) {
                ApiResponse<Object> projectNotFound = new ApiResponse<>("Project not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectNotFound);
            }

            // CHECK IF USER EXISTS
            UserDTO userExists = userRepository.findUserById(form.getUserId());

            // IF USER DOES NOT EXIST
            if (userExists == null) {
                ApiResponse<Object> userNotFound = new ApiResponse<>("User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
            }

            // CHECK IF FORM ALREADY EXISTS
            FormDTO formExistsByName = formRepository.findByNameAndProjectId(form.getName(), form.getProjectId());

            if (formExistsByName != null && !formExistsByName.getId().equals(id)) {
                ApiResponse<Object> formConflict = new ApiResponse<>("Form with this name already exists", stringsUtils.mapIdToObject(formExistsByName.getId()));
                return ResponseEntity.status(HttpStatus.CONFLICT).body(formConflict);
            }

            formExists.setName(form.getName());
            formExists.setDescription(form.getDescription());
            formExists.setVisibility(form.getVisibility());
            formExists.setProjectId(form.getProjectId());
            formExists.setUserId(form.getUserId());

            ApiResponse<Object> formUpdated = new ApiResponse<>("Form updated successfully", formRepository.updateForm(
                    formExists.getName(),
                    formExists.getDescription(),
                    formExists.getVisibility(),
                    formExists.isActive(),
                    formExists.getProjectId(),
                    formExists.getUserId(),
                    id
            ));
            return ResponseEntity.status(HttpStatus.OK).body(formUpdated);
        } catch (Exception e) {
            ApiResponse<Object> serverError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError);
        }
    }

    // DELETE FORM
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteForm(@PathVariable UUID id) {
        try {
            FormDTO formExists = formRepository.findFormById(id);
            if (formExists == null) {
                ApiResponse<Object> formNotFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formNotFound);
            }
            formRepository.deleteById(id);
            ApiResponse<Object> formDeleted = new ApiResponse<>("Form deleted successfully", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(formDeleted);
        } catch (Exception e) {
            ApiResponse<Object> serverError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError);
        }
    }
}
