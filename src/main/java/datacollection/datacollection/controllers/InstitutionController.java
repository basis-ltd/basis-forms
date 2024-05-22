package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.CategoryDTO;
import datacollection.datacollection.dtos.InstitutionDTO;
import datacollection.datacollection.entities.Institution;
import datacollection.datacollection.repositories.CategoryRepository;
import datacollection.datacollection.repositories.InstitutionRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.StringsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/institutions")
@CrossOrigin(origins = "*")
public class InstitutionController {

    private final InstitutionRepository institutionRepository;
    private final CategoryRepository categoryRepository;
    private final StringsUtils stringsUtils = new StringsUtils();

    public InstitutionController(InstitutionRepository institutionRepository, CategoryRepository categoryRepository) {
        this.institutionRepository = institutionRepository;
        this.categoryRepository = categoryRepository;
    }

    // FETCH INSTITUTIONS
    @GetMapping(value = "")
    public ResponseEntity<Object> fetchInstitutions(@RequestParam(required = false) UUID categoryId) {
        try {
            if (categoryId != null) {
                CategoryDTO categoryExists = categoryRepository.findCategoryById(categoryId);
                if (categoryExists == null) {
                    ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                    return ResponseEntity.status(404).body(categoryNotFound);
                }
                ApiResponse<Object> categoryInstitutions = new ApiResponse<>("Institutions found successfully", institutionRepository.findInstitutionByCategoryId(categoryId));
                return ResponseEntity.status(200).body(categoryInstitutions);
            }
            ApiResponse<Object> allInstitutions = new ApiResponse<>("Institutions found successfully", institutionRepository.findAllInstitutions());
            return ResponseEntity.status(200).body(allInstitutions);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // CREATE INSTITUTION
    @PostMapping(value = "")
    public ResponseEntity<Object> createInstitution(@RequestBody Institution institution) {
        try {
            // CHECK IF INSTITUTION EXISTS
            InstitutionDTO institutionExists = institutionRepository.findByEmail(institution.getEmail());
            if (institutionExists != null) {
                ApiResponse<Object> institutionConflict = new ApiResponse<>("Institution already exists", stringsUtils.mapIdToObject(institutionExists.getId()));
                return ResponseEntity.status(409).body(institutionConflict);
            }

            // CHECK IF CATEGORY EXISTS
            CategoryDTO categoryExists = categoryRepository.findCategoryById(institution.getCategoryId());
            if (categoryExists == null) {
                ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                return ResponseEntity.status(404).body(categoryNotFound);
            }

            Institution newInstitution = getNewInstitution(institution);
            institutionRepository.save(newInstitution);
            ApiResponse<Object> institutionCreated = new ApiResponse<>("Institution created successfully", newInstitution);
            return ResponseEntity.status(201).body(institutionCreated);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    private static Institution getNewInstitution(Institution institution) {
        Institution newInstitution = new Institution();
        newInstitution.setName(institution.getName());
        newInstitution.setCategoryId(institution.getCategoryId());
        newInstitution.setAddress(institution.getAddress());
        newInstitution.setPhone(institution.getPhone());
        newInstitution.setEmail(institution.getEmail());
        return newInstitution;
    }

    // DELETE INSTITUTION
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteInstitution(@PathVariable UUID id) {
        try {
            Institution institutionExists = institutionRepository.findInstitutionToDelete(id);
            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return ResponseEntity.status(404).body(institutionNotFound);
            }
            institutionRepository.delete(institutionExists);
            ApiResponse<Object> institutionDeleted = new ApiResponse<>("Institution deleted successfully", null);
            return ResponseEntity.status(204).body(institutionDeleted);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // GET INSTITUTION BY ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getInstitutionById(@PathVariable UUID id) {
        try {
            InstitutionDTO institutionExists = institutionRepository.findInstitutionById(id);
            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return ResponseEntity.status(404).body(institutionNotFound);
            }
            ApiResponse<Object> institutionFound = new ApiResponse<>("Institution found successfully", institutionExists);
            return ResponseEntity.status(200).body(institutionFound);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // UPDATE INSTITUTION
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateInstitution(@PathVariable UUID id, @RequestBody Institution institution) {
        try {
            Institution institutionExists = institutionRepository.findInstitutionToUpdate(id);
            if (institutionExists == null) {
                ApiResponse<Object> institutionNotFound = new ApiResponse<>("Institution not found", null);
                return ResponseEntity.status(404).body(institutionNotFound);
            }

            // CHECK IF CATEGORY EXISTS
            CategoryDTO categoryExists = categoryRepository.findCategoryById(institution.getCategoryId());
            if (categoryExists == null) {
                ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                return ResponseEntity.status(404).body(categoryNotFound);
            }

            Institution updatedInstitution = getUpdatedInstitution(institutionExists, institution);
            institutionRepository.save(updatedInstitution);
            ApiResponse<Object> institutionUpdated = new ApiResponse<>("Institution updated successfully", updatedInstitution);
            return ResponseEntity.status(200).body(institutionUpdated);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    private Institution getUpdatedInstitution(Institution institutionExists, Institution institution) {
        institutionExists.setName(institution.getName());
        institutionExists.setCategoryId(institution.getCategoryId());
        institutionExists.setAddress(institution.getAddress());
        institutionExists.setPhone(institution.getPhone());
        institutionExists.setEmail(institution.getEmail());
        return institutionExists;
    }
}
