package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.FormSectionDTO;
import datacollection.datacollection.dtos.SectionDTO;
import datacollection.datacollection.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {

    // FIND SECTION BY NAME AND FORM ID
    @Query("SELECT new datacollection.datacollection.dtos.FormSectionDTO(s.id, s.name, s.description, s.sequence, s.formId, s.createdAt, s.updatedAt) FROM Section s WHERE s.name = :name AND s.formId = :formId")
    FormSectionDTO findByNameAndFormId(String name, UUID formId);

    // FIND SECTIONS BY FORM ID
    @Query("SELECT new datacollection.datacollection.dtos.FormSectionDTO(s.id, s.name, s.description, s.sequence, s.formId, s.createdAt, s.updatedAt) FROM Section s WHERE s.formId = :formId order by s.sequence asc")
    List<FormSectionDTO> findByFormId(UUID formId);

    // COUNT SECTIONS BY FORM ID
    @Query("SELECT COUNT(s) FROM Section s WHERE s.formId = :formId")
    int countByFormId(UUID formId);

    // LIST SECTIONS BY SEQUENCE
    @Query("SELECT new datacollection.datacollection.dtos.SectionDTO(s.id, s.name, s.description, s.sequence, s.formId, s.createdAt, s.updatedAt, null) FROM Section s WHERE s.formId = :formId AND s.sequence BETWEEN :min AND :max")
    List<SectionDTO> findByFormIdAndSequenceBetween(UUID formId, int min, int max);

    // UPDATE SECTION SEQUENCE
    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.sequence = :sequence WHERE s.id = :id")
    void updateSectionSequence(UUID id, int sequence);

    // UPDATE SECTION
    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.name = :name, s.description = :description WHERE s.id = :id")
    int updateSection(UUID id, String name, String description);

    // FIND SECTION BY ID
    @Query("SELECT new datacollection.datacollection.dtos.SectionDTO(s.id, s.name, s.description, s.sequence, s.formId, s.createdAt, s.updatedAt," +
            " new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, (SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, null, null, null)) FROM Section s " +
            "LEFT JOIN s.form f WHERE s.id = :id")
    SectionDTO findSectionById(UUID id);
}
