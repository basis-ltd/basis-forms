package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // FIND BY EMAIL
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.password, u.role, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(u.institution.id as id, u.institution.name as name, u.institution.createdAt as createdAt, u.institution.updatedAt as updatedAt, u.institution.isActive as isActive)"+
            ") " +
            "FROM User u " +
            "WHERE u.email = ?1")
    UserDTO findByEmail(String email);


    List<User> findByInstitutionId(UUID institutionId);

    List<User> findByRole(String role);
}
