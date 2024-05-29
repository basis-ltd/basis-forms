package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.EntryDTO;
import datacollection.datacollection.dtos.FormDTO;
import datacollection.datacollection.entities.Entry;
import datacollection.datacollection.repositories.EntryRepository;
import datacollection.datacollection.repositories.FormRepository;
import datacollection.datacollection.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin(origins = "*")
public class EntryController {

    private final FormRepository formRepository;
    private final EntryRepository entryRepository;

    public EntryController(FormRepository formRepository, EntryRepository entryRepository) {
        this.formRepository = formRepository;
        this.entryRepository = entryRepository;
    }

    // CREATE ENTRY
    @PostMapping("")
    public ResponseEntity<Object> createEntry(@RequestBody EntryDTO entry) {
        try {
            // CHECK IF REQUIRED FIELDS ARE PRESENT
            if (entry.getFormId() == null) {
                return ResponseEntity.badRequest().body("Form ID is required.");
            }

            // CHECK IF FORM EXISTS IN DATABASE
            FormDTO formExists = formRepository.findFormById(entry.getFormId());

            if (formExists == null) {
                ApiResponse<Object> formNotFound = new ApiResponse<>("Form not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formNotFound);
            }

            Entry newEntry = new Entry();
            newEntry.setStatus("IN_PROGRESS");
            newEntry.setProgress(0);
            newEntry.setFormId(entry.getFormId());
            newEntry.setUserId(entry.getUserId());

            ApiResponse<Object> entryCreated = new ApiResponse<>("Entry created successfully", entryRepository.save(newEntry));
            return ResponseEntity.status(HttpStatus.OK).body(entryCreated);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // FIND ENTRY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findEntryById(@PathVariable UUID id) {
        try {
            EntryDTO entryExists = entryRepository.findEntryById(id);

            if (entryExists == null) {
                ApiResponse<Object> entryNotFound = new ApiResponse<>("Entry not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entryNotFound);
            }

            ApiResponse<Object> entryFound = new ApiResponse<>("Entry found", entryExists);
            return ResponseEntity.status(HttpStatus.OK).body(entryFound);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // FETCH ENTRIES
    @GetMapping("")
    public ResponseEntity<Object> findEntriesByFormId(@RequestParam(value = "formId", required = false) UUID formId, @RequestParam(value = "userId", required = false) UUID userId, @RequestParam(value = "status", required = false) String status) {
        try {
            // CHECK IF FORM ID IS PRESENT
            if (formId == null) {
                ApiResponse<Object> formIdRequired = new ApiResponse<>("Form ID is required", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formIdRequired);
            }
            if (userId == null && status == null) {
                ApiResponse<Object> entriesFound = new ApiResponse<>("Entries found", entryRepository.findEntriesByFormId(formId));
                return ResponseEntity.status(HttpStatus.OK).body(entriesFound);
            }
            if (status != null && userId != null) {
                ApiResponse<Object> entriesFound = new ApiResponse<>("Entries found", entryRepository.findEntriesByFormIdAndUserIdAndStatus(formId, userId, status));
                return ResponseEntity.status(HttpStatus.OK).body(entriesFound);
            }
            ApiResponse<Object> entriesFound = new ApiResponse<>("Entries found", entryRepository.findEntriesByFormIdAndUserId(formId, userId));
            return ResponseEntity.status(HttpStatus.OK).body(entriesFound);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // UPDATE ENTRY
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateEntry(@PathVariable UUID id, @RequestBody EntryDTO entry) {
        try {
            EntryDTO entryExists = entryRepository.findEntryById(id);

            if (entryExists == null) {
                ApiResponse<Object> entryNotFound = new ApiResponse<>("Entry not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entryNotFound);
            }

            Entry updateEntry = new Entry();
            updateEntry.setId(id);
            updateEntry.setStatus(entry.getStatus());
            updateEntry.setProgress(entry.getProgress());
            updateEntry.setFormId(entry.getFormId());
            updateEntry.setUserId(entry.getUserId());

            ApiResponse<Object> entryUpdated = new ApiResponse<>("Entry updated successfully", entryRepository.save(updateEntry));
            return ResponseEntity.status(HttpStatus.OK).body(entryUpdated);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }

    // DELETE ENTRY
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntry(@PathVariable UUID id) {
        try {
            EntryDTO entryExists = entryRepository.findEntryById(id);

            if (entryExists == null) {
                ApiResponse<Object> entryNotFound = new ApiResponse<>("Entry not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entryNotFound);
            }

            entryRepository.deleteById(id);

            ApiResponse<Object> entryDeleted = new ApiResponse<>("Entry deleted successfully", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(entryDeleted);
        } catch (Exception e) {
            ApiResponse<Object> internalServerError = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerError);
        }
    }
}
