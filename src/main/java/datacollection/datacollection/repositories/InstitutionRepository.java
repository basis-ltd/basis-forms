package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.InstitutionDTO;
import datacollection.datacollection.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID> {
    Institution findByName(String name);

    @Query("SELECT new datacollection.datacollection.dtos.InstitutionDTO(" +
            "i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt) " +
            "FROM Institution i " +
            "WHERE i.email = ?1")
    InstitutionDTO findByEmail(String email);

    @Query("SELECT new datacollection.datacollection.dtos.InstitutionDTO(" +
            "i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt) " +
            "FROM Institution i " +
            "WHERE i.id = ?1")
    InstitutionDTO findInstitutionById(UUID id);

    @Query("SELECT i FROM Institution i WHERE i.categoryId = ?1")
    List<Institution> findInstitutionByCategoryId(UUID category);

    @Query("SELECT new datacollection.datacollection.dtos.InstitutionDTO(" +
            "i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt) " +
            "FROM Institution i ")
    List<InstitutionDTO> findAllInstitutions();

    @Query("SELECT i FROM Institution i WHERE i.id = ?1")
    Institution getInstitutionDetails(UUID id);
}