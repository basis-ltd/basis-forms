package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.UserAuthDTO;
import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // FIND BY EMAIL
    @Query("SELECT new datacollection.datacollection.dtos.UserAuthDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.password, u.role, u.isActive, u.institutionId, u.createdAt, u.updatedAt, " +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.email = ?1")
    UserAuthDTO findByEmail(String email);

    // FIND BY INSTITUTION ID
    List<UserDTO> findByInstitutionId(UUID institutionId);

    // FIND BY ROLE
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.role, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.role = ?1")
    List<UserDTO> findByRole(String role);

    // FIND BY ID
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.role, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.id = ?1")
    UserDTO findUserById(UUID id);
}
