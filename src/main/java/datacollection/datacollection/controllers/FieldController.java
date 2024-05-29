package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.FieldDTO;
import datacollection.datacollection.dtos.SectionDTO;
import datacollection.datacollection.entities.Field;
import datacollection.datacollection.repositories.FieldRepository;
import datacollection.datacollection.repositories.SectionRepository;
import datacollection.datacollection.services.FieldService;
import datacollection.datacollection.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "*")
public class FieldController {

    private final SectionRepository sectionRepository;
    private static FieldRepository fieldRepository;
    private final FieldService fieldService;

    public FieldController(SectionRepository sectionRepository, FieldRepository fieldRepository, FieldService fieldService) {
        this.sectionRepository = sectionRepository;
        FieldController.fieldRepository = fieldRepository;
        this.fieldService = fieldService;
    }

    // CREATE FIELD
    @PostMapping("")
    public ResponseEntity<Object> createField(@RequestBody FieldDTO field) {
        try {
            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(field.getSectionId());
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            // CHECK IF FIELD WITH LABEL AND SECTION ID EXISTS
            FieldDTO fieldExists = fieldRepository.findBySectionIdAndLabel(field.getSectionId(), field.getLabel());
            if (fieldExists != null) {
                ApiResponse<Object> conflict = new ApiResponse<>("Field with label already exists", null);
                return ResponseEntity.status(409).body(conflict);
            }

            // CREATE FIELD
            Field newField = getNewField(field);
            fieldRepository.save(newField);

            ApiResponse<Object> created = new ApiResponse<>("Field created", newField);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // CREATE MULTIPLE FIELDS
    @PostMapping("/multiple")
    public ResponseEntity<Object> createMultipleFields(@RequestBody FieldDTO[] fields) {
        try {
            for (FieldDTO field : fields) {
                // CHECK IF SECTION EXISTS
                SectionDTO sectionExists = sectionRepository.findSectionById(field.getSectionId());
                if (sectionExists == null) {
                    ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                    return ResponseEntity.status(404).body(notFound);
                }

                // CHECK IF FIELD WITH LABEL AND SECTION ID EXISTS
                FieldDTO fieldExists = fieldRepository.findBySectionIdAndLabel(field.getSectionId(), field.getLabel());
                if (fieldExists != null) {
                    ApiResponse<Object> conflict = new ApiResponse<>("Field with label already exists", null);
                    return ResponseEntity.status(409).body(conflict);
                }

                // CREATE FIELD
                Field newField = getNewField(field);
                fieldRepository.save(newField);
            }

            ApiResponse<Object> created = new ApiResponse<>("Fields created", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    private static Field getNewField(FieldDTO field) {

        // COUNT SECTION FIELDS
        int count = fieldRepository.countBySectionId(field.getSectionId());

        Field newField = new Field();
        newField.setSequence(count + 1);
        newField.setName(field.getName());
        newField.setLabel(field.getLabel());
        newField.setPlaceholder(field.getPlaceholder());
        newField.setRequired(field.isRequired());
        newField.setType(field.getType());
        newField.setAccept(field.getAccept());
        newField.setDisabled(field.isDisabled());
        newField.setReadonly(field.isReadonly());
        newField.setMaxLength(field.getMaxLength());
        newField.setMinLength(field.getMinLength());
        newField.setMax(field.getMax());
        newField.setMin(field.getMin());
        newField.setPattern(field.getPattern());
        newField.setMultiple(field.isMultiple());
        newField.setDefaultValue(field.getDefaultValue());
        newField.setAutocapitalize(field.isAutocapitalize());
        newField.setSectionId(field.getSectionId());
        newField.setUserId(field.getUserId());
        return newField;
    }

    // FETCH ALL FIELDS
    @GetMapping("")
    public ResponseEntity<Object> fetchAllFields(@RequestParam(
            value = "sectionId",
            required = false
    ) UUID sectionId
    ) {
        try {
            // CHECK IF ALL REQUIRED PARAMS ARE PROVIDED
            if (sectionId == null) {
                ApiResponse<Object> badRequest = new ApiResponse<>("Section ID is required", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequest);
            }

            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(sectionId);
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            ApiResponse<Object> sectionFields = new ApiResponse<>("Fields fetched", fieldRepository.findBySectionId(sectionId));
            return ResponseEntity.status(HttpStatus.OK).body(sectionFields);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // FETCH FIELD BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchFieldById(@PathVariable UUID id) {
        try {
            // CHECK IF FIELD EXISTS
            FieldDTO fieldExists = fieldRepository.findFieldById(id);
            if (fieldExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Field not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            ApiResponse<Object> field = new ApiResponse<>("Field fetched", fieldExists);
            return ResponseEntity.status(HttpStatus.OK).body(field);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // UPDATE FIELD
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateField(@PathVariable UUID id, @RequestBody FieldDTO field) {
        try {
            // CHECK IF FIELD EXISTS
            FieldDTO fieldExists = fieldRepository.findFieldById(id);
            if (fieldExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Field not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            // CHECK IF SECTION EXISTS
            SectionDTO sectionExists = sectionRepository.findSectionById(field.getSectionId());
            if (sectionExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Section not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            // CHECK IF FIELD WITH LABEL AND SECTION ID EXISTS
            FieldDTO fieldWithLabelExists = fieldRepository.findBySectionIdAndLabel(field.getSectionId(), field.getLabel());
            if (fieldWithLabelExists != null && !fieldWithLabelExists.getId().equals(id)) {
                ApiResponse<Object> conflict = new ApiResponse<>("Field with label already exists", null);
                return ResponseEntity.status(409).body(conflict);
            }

            // IF SEQUENCE IS CHANGED
            if (fieldExists.getSequence() !=0 && fieldExists.getSequence() != field.getSequence()) {
                // UPDATE FIELD SEQUENCE
                fieldService.updateFieldSequence(fieldExists, field.getSequence());
            }

            ApiResponse<Object> updated = new ApiResponse<>("Field updated", fieldRepository.updateField(
                    id,
                    field.getName(),
                    field.getLabel(),
                    field.getPlaceholder(),
                    field.isRequired(),
                    field.getType(),
                    field.getAccept(),
                    field.isDisabled(),
                    field.isReadonly(),
                    field.getMaxLength(),
                    field.getMinLength(),
                    field.getMax(),
                    field.getMin(),
                    field.getPattern(),
                    field.isMultiple(),
                    field.getDefaultValue(),
                    field.isAutocapitalize(),
                    LocalDateTime.now()
            ));
            return ResponseEntity.status(HttpStatus.OK).body(updated);

        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // DELETE FIELD
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteField(@PathVariable UUID id) {
        try {
            // CHECK IF FIELD EXISTS
            FieldDTO fieldExists = fieldRepository.findFieldById(id);
            if (fieldExists == null) {
                ApiResponse<Object> notFound = new ApiResponse<>("Field not found", null);
                return ResponseEntity.status(404).body(notFound);
            }

            // COUNT SECTION FIELDS
            int count = fieldRepository.countBySectionId(fieldExists.getSectionId());

            // UPDATE FIELD SEQUENCE TO MAKE IT LAST
            fieldService.updateFieldSequence(fieldExists, count);

            // DELETE FIELD
            fieldRepository.deleteById(id);

            ApiResponse<Object> deleted = new ApiResponse<>("Field deleted", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleted);

        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }
}
