package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.FormDTO;
import datacollection.datacollection.entities.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {

    // FIND FORM BY NAME AND PROJECT ID
    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, (SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, null, null, null) FROM Form f WHERE f.name = ?1 AND f.projectId = ?2")
    FormDTO findByNameAndProjectId(String name, UUID projectId);

    // FIND ALL FORMS
    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, (SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt), null) " +
            "FROM Form f " +
            "JOIN Project p ON f.projectId = p.id " +
            "JOIN User u ON f.userId = u.id ORDER BY f.createdAt DESC")
    List<FormDTO> findAllForms();

    // FIND FORMS BY PROJECT ID
    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, (SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt), null) " +
            "FROM Form f " +
            "JOIN Project p ON f.projectId = p.id " +
            "JOIN User u ON f.userId = u.id " +
            "WHERE f.projectId = ?1 ORDER BY f.createdAt DESC")
    List<FormDTO> findFormsByProjectId(UUID projectId);

    // FIND FORMS BY USER ID
    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, (SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt), null) " +
            "FROM Form f " +
            "JOIN Project p ON f.projectId = p.id " +
            "JOIN User u ON f.userId = u.id " +
            "WHERE f.userId = ?1 ORDER BY f.createdAt DESC")
    List<FormDTO> findFormsByUserId(UUID userId);

    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, " +
            "(SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), " +
            "f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt), null) " +
            "FROM Form f " +
            "JOIN Project p ON f.projectId = p.id " +
            "JOIN User u ON f.userId = u.id " +
            "WHERE f.projectId = ?1 AND f.userId = ?2 ORDER BY f.createdAt DESC")
    List<FormDTO> findFormsByProjectIdAndUserId(UUID projectId, UUID userId);

    // FIND FORM BY ID
    @Query("SELECT new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, null, f.createdAt, f.updatedAt, " +
            "new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt), null) " +
            "FROM Form f " +
            "JOIN Project p ON f.projectId = p.id " +
            "JOIN User u ON f.userId = u.id " +
            "WHERE f.id = ?1")
    FormDTO findFormById(UUID formId);

    // UPDATE FORM
    @Modifying
    @Transactional
    @Query("UPDATE Form f SET f.name = ?1, f.description = ?2, f.visibility = ?3, f.isActive = ?4, f.projectId = ?5, f.userId = ?6 WHERE f.id = ?7")
    int updateForm(String name, String description, String visibility, boolean isActive, UUID projectId, UUID userId, UUID formId);

}
