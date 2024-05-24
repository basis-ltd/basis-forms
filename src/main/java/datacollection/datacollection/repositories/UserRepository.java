package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.UserAuthDTO;
import datacollection.datacollection.dtos.UserDTO;
import datacollection.datacollection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // FIND BY EMAIL
    @Query("SELECT new datacollection.datacollection.dtos.UserAuthDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.password, u.role, u.phone, u.isActive, u.institutionId, u.createdAt, u.updatedAt, " +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.email = ?1")
    UserAuthDTO findByEmail(String email);

    // FIND BY ID
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.role, u.phone, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.id = ?1")
    UserDTO findUserById(UUID id);

    // UPDATE USER
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = ?1, u.lastName = ?2, u.role = ?3, u.phone = ?4, u.isActive = ?5, u.institutionId = ?6, u.updatedAt = ?7 WHERE u.id = ?8")
    int updateUser(String firstName, String lastName, String role, String phone, boolean isActive, UUID institutionId, LocalDateTime updatedAt, UUID id);

    // FIND USER BY ROLE OR INSTITUTION ID OR BOTH
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.role, u.phone, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i " +
            "WHERE u.role = ?1 OR u.institutionId = ?2")
    List<UserDTO> findByRoleOrInstitutionId(String role, UUID institutionId);

    // FIND ALL USERS
    @Query("SELECT new datacollection.datacollection.dtos.UserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, u.role, u.phone, u.isActive, u.institutionId, u.createdAt, u.updatedAt," +
            "new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt)) " +
            "FROM User u " +
            "LEFT JOIN u.institution i ORDER BY u.updatedAt DESC")
    List<UserDTO> findAllUsers();
}
