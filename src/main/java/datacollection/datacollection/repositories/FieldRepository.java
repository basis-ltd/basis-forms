package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.FieldDTO;
import datacollection.datacollection.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {

    @Query("SELECT new datacollection.datacollection.dtos.FieldDTO(f.id, f.sequence, f.name, f.label, f.placeholder, f.required, f.type, f.accept, f.disabled, f.readonly, f.maxLength, f.minLength, f.max, f.min, f.pattern, f.multiple, f.defaultValue, f.autocapitalize, f.sectionId, f.userId, f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.FormSectionDTO(s.id, s.name, s.description, s.sequence, s.formId, s.createdAt, s.updatedAt), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) " +
            "FROM Field f " +
            "JOIN f.section s " +
            "JOIN f.user u " +
            "WHERE f.id = :id")
    FieldDTO findFieldById(UUID id);

    // FIND FIELD BY SECTION ID AND LABEL
    @Query("SELECT new datacollection.datacollection.dtos.FieldDTO(f.id, f.sequence, f.name, f.label, f.placeholder, f.required, f.type, f.accept, f.disabled, f.readonly, f.maxLength, f.minLength, f.max, f.min, f.pattern, f.multiple, f.defaultValue, f.autocapitalize, f.sectionId, f.userId, f.createdAt, f.updatedAt, null, null) FROM Field f WHERE f.sectionId = :sectionId AND f.label = :label order by f.sequence asc")
    FieldDTO findBySectionIdAndLabel(UUID sectionId, String label);

    // FIND FIELDS BY SECTION ID
    @Query("SELECT new datacollection.datacollection.dtos.FieldDTO(f.id, f.sequence, f.name, f.label, f.placeholder, f.required, f.type, f.accept, f.disabled, f.readonly, f.maxLength, f.minLength, f.max, f.min, f.pattern, f.multiple, f.defaultValue, f.autocapitalize, f.sectionId, f.userId, f.createdAt, f.updatedAt, null, null) FROM Field f WHERE f.sectionId = :sectionId order by f.sequence asc")
    List<FieldDTO> findBySectionId(UUID sectionId);

    // COUNT SECTION FIELDS
    @Query("SELECT COUNT(f) FROM Field f WHERE f.sectionId = :sectionId")
    int countBySectionId(UUID sectionId);

    // UPDATE FIELD SEQUENCE
    @Modifying
    @Transactional
    @Query("UPDATE Field f SET f.sequence = :sequence WHERE f.id = :id")
    void updateFieldSequence(UUID id, int sequence);

    @Query("SELECT new datacollection.datacollection.dtos.FieldDTO(f.id, f.sequence, f.name, f.label, f.placeholder, f.required, f.type, f.accept, f.disabled, f.readonly, f.maxLength, f.minLength, f.max, f.min, f.pattern, f.multiple, f.defaultValue, f.autocapitalize, f.sectionId, f.userId, f.createdAt, f.updatedAt, null, null) FROM Field f WHERE f.sectionId = :sectionId AND f.sequence BETWEEN :min AND :max")
    List<FieldDTO> findBySectionIdAndSequenceBetween(UUID sectionId, int min, int max);

    // UPDATE FIELD
    @Modifying
    @Transactional
    @Query("UPDATE Field f SET f.name = :name, f.label = :label, f.placeholder = :placeholder, f.required = :required, f.type = :type, f.accept = :accept, f.disabled = :disabled, f.readonly = :readonly, f.maxLength = :maxLength, f.minLength = :minLength, f.max = :max, f.min = :min, f.pattern = :pattern, f.multiple = :multiple, f.defaultValue = :defaultValue, f.autocapitalize = :autocapitalize, f.updatedAt = :updatedAt WHERE f.id = :id")
    int updateField(UUID id, String name, String label, String placeholder, boolean required, String type, String accept, boolean disabled, boolean readonly, int maxLength, int minLength, int max, int min, String pattern, boolean multiple, String defaultValue, boolean autocapitalize, LocalDateTime updatedAt);
}
