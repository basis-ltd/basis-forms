package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.FormDTO;
import datacollection.datacollection.dtos.FormSectionDTO;
import datacollection.datacollection.dtos.SectionDTO;
import datacollection.datacollection.entities.Section;
import datacollection.datacollection.repositories.FormRepository;
import datacollection.datacollection.repositories.SectionRepository;
import datacollection.datacollection.services.SectionService;
import datacollection.datacollection.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/sections")
@CrossOrigin(origins = "*")
public class SectionController {

    // REPOSITORIES
    private final FormRepository formRepository;
    private final SectionRepository sectionRepository;
    private final SectionService sectionService;

    public SectionController(FormRepository formRepository, SectionRepository sectionRepository, SectionService sectionService) {
        this.formRepository = formRepository;
        this.sectionRepository = sectionRepository;
        this.sectionService = sectionService;
    }

    // CREATE SECTION
    @PostMapping(value = "")
    public ResponseEntity<Object> createSection(@RequestBody FormSectionDTO section) {
        try {
            // CHECK REQUIRED PARAMETERS HAVE BEEN PROVIDED
            if (section.getName() == null || section.getFormId() == null) {
                ApiResponse<Object> badRequest = new ApiResponse<>("Name and form ID are required", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequest);
            }

            // CHECK IF FORM EXISTS
            FormDTO formExists = formRepository.findFormById(section.getFormId());

            // IF FORM DOES NOT EXIST
            if (formExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }

            // CHECK IF SECTION EXISTS
            FormSectionDTO sectionExists = sectionRepository.findByNameAndFormId(section.getName(), section.getFormId());

            // IF SECTION EXISTS
            if (sectionExists != null) {
                ApiResponse<Object> conflict = new ApiResponse<>("Section with this name already exists in the form", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(conflict);
            }

            // COUNT FORM SECTIONS TO OBTAIN SEQUENCE
            int countSections = sectionRepository.countByFormId(section.getFormId());

            // CREATE SECTION
            Section newSection = new Section();
            newSection.setName(section.getName());
            newSection.setDescription(section.getDescription());
            newSection.setFormId(section.getFormId());
            newSection.setSequence(countSections + 1);

            ApiResponse<Object> createdSection = new ApiResponse<>("Section created successfully", sectionRepository.save(newSection));

            return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // FETCH SECTIONS
    @GetMapping(value = "")
    public ResponseEntity<Object> fetchSections(@RequestParam(value = "formId", required = false) UUID formId) {
        try {

            if (formId == null) {
                ApiResponse<Object> badRequest = new ApiResponse<>("Form ID is required", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequest);
            }

            // CHECK IF FORM EXISTS
            FormDTO formExists = formRepository.findFormById(formId);

            // IF FORM DOES NOT EXIST
            if (formExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }

            // FETCH SECTIONS
            ApiResponse<Object> fetchedSections = new ApiResponse<>("Sections fetched successfully", sectionRepository.findByFormId(formId));

            return ResponseEntity.status(HttpStatus.OK).body(fetchedSections);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // UPDATE SECTIONS
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateSection(@PathVariable("id") UUID id, @RequestBody FormSectionDTO section) {
        try {
            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(id);

            // IF SECTION DOES NOT EXIST
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }

            // CHECK IF SECTION NAME EXISTS
            FormSectionDTO sectionNameExists = sectionRepository.findByNameAndFormId(section.getName(), sectionExists.getFormId());

            // IF SECTION NAME EXISTS
            if (sectionNameExists != null && !sectionNameExists.getId().equals(id)) {
                ApiResponse<Object> conflict = new ApiResponse<>("Section with this name already exists in the form", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(conflict);
            }

            // UPDATE SECTION
            sectionExists.setName(section.getName());
            sectionExists.setDescription(section.getDescription());

            // CHECK IF SEQUENCE HAS BEEN UPDATED
            if (section.getSequence() !=0 && section.getSequence() != sectionExists.getSequence()) {
                sectionService.updateSectionSequence(sectionExists, section.getSequence());
            }

            ApiResponse<Object> updatedSection = new ApiResponse<>("Section updated successfully", sectionRepository.updateSection(
                    id, section.getName(), section.getDescription()
            ));

            return ResponseEntity.status(HttpStatus.OK).body(updatedSection);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // GET SECTION BY ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getSectionById(@PathVariable("id") UUID id) {
        try {
            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(id);

            // IF SECTION DOES NOT EXIST
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }

            ApiResponse<Object> fetchedSection = new ApiResponse<>("Section fetched successfully", sectionExists);

            return ResponseEntity.status(HttpStatus.OK).body(fetchedSection);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // DELETE SECTION
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSection(@PathVariable("id") UUID id) {
        try {
            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(id);

            // IF SECTION DOES NOT EXIST
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }

            // COUNT FORM SECTIONS
            int countSections = sectionRepository.countByFormId(sectionExists.getFormId());

            // UPDATE CURRENT SECTION'S SEQUENCE
            sectionService.updateSectionSequence(sectionExists, countSections + 1);

            // DELETE SECTION
            sectionRepository.deleteById(id);

            ApiResponse<Object> deletedSection = new ApiResponse<>("Section deleted successfully", null);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deletedSection);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }
}
