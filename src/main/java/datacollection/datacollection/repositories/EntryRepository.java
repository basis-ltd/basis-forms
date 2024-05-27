package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.EntryDTO;
import datacollection.datacollection.entities.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {

    @Query("SELECT new datacollection.datacollection.dtos.EntryDTO(e.id, e.status, e.progress, e.formId, e.userId, e.createdAt, e.updatedAt, null, null) FROM Entry e WHERE e.formId = :formId AND e.userId = :userId")
    List<EntryDTO> findEntriesByFormIdAndUserId(UUID formId, UUID userId);

    @Query("SELECT new datacollection.datacollection.dtos.EntryDTO(e.id, e.status, e.progress, e.formId, e.userId, e.createdAt, e.updatedAt, " +
            "new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, " +
            "(SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, null, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) " +
            "FROM Entry e " +
            "JOIN Form f ON e.formId = f.id " +
            "LEFT JOIN User u ON e.userId = u.id " +
            "WHERE e.formId = :formId AND e.userId = :userId AND e.status = :status order by e.updatedAt desc")
    List<EntryDTO> findEntriesByFormIdAndUserIdAndStatus(UUID formId, UUID userId, String status);

    // FIND ENTRY BY ID
    @Query("SELECT new datacollection.datacollection.dtos.EntryDTO(e.id, e.status, e.progress, e.formId, e.userId, e.createdAt, e.updatedAt, " +
            "new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, " +
            "(SELECT COUNT(s.id) FROM Section s WHERE s.formId = f.id), f.createdAt, f.updatedAt, null, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) " +
            "FROM Entry e " +
            "JOIN Form f ON e.formId = f.id " +
            "LEFT JOIN User u ON e.userId = u.id " +
            "WHERE e.id = :id")
    EntryDTO findEntryById(UUID id);

    // FIND ENTRIES BY FORM ID
    @Query("SELECT new datacollection.datacollection.dtos.EntryDTO(e.id, e.status, e.progress, e.formId, e.userId, e.createdAt, e.updatedAt, " +
            "new datacollection.datacollection.dtos.FormDTO(f.id, f.name, f.description, f.visibility, f.isActive, f.projectId, f.userId, null, f.createdAt, f.updatedAt, null, null, null), " +
            "new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) " +
            "FROM Entry e " +
            "JOIN Form f ON e.formId = f.id " +
            "LEFT JOIN User u ON e.userId = u.id " +
            "WHERE e.formId = :formId OR e.userId = null order by e.updatedAt desc")
    List<EntryDTO> findEntriesByFormId(UUID formId);
}
