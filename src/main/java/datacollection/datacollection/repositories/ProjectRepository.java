package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.ProjectDTO;
import datacollection.datacollection.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    // FIND ALL PROJECTS
    @Query("""
            SELECT new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt), new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) FROM Project p JOIN Institution i ON p.institutionId = i.id JOIN User u ON p.userId = u.id""")
    List<ProjectDTO> findAllProjects();

    // FIND PROJECT BY NAME AND DESCRIPTION
    @Query("""
            SELECT new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt), new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) FROM Project p JOIN Institution i ON p.institutionId = i.id JOIN User u ON p.userId = u.id WHERE p.name = ?1 AND p.description = ?2""")
    ProjectDTO findProjectByNameAndDescription(String name, String description);

    @Query("""
            SELECT new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt), new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) FROM Project p JOIN Institution i ON p.institutionId = i.id JOIN User u ON p.userId = u.id WHERE p.id = ?1""")
    ProjectDTO findProjectById(UUID id);

    // FIND PROJECTS BY INSTITUTION ID
    @Query("""
            SELECT new datacollection.datacollection.dtos.ProjectDTO(p.id, p.name, p.description, p.startDate, p.endDate, p.isActive, p.institutionId, p.userId, p.createdAt, p.updatedAt, new datacollection.datacollection.dtos.InstitutionDTO(i.id, i.name, i.description, i.email, i.phone, i.address, i.isActive, i.categoryId, i.createdAt, i.updatedAt), new datacollection.datacollection.dtos.CreatedByDTO(u.id, u.firstName, u.lastName, u.email, u.role, u.institutionId, u.createdAt, u.updatedAt)) FROM Project p JOIN Institution i ON p.institutionId = i.id JOIN User u ON p.userId = u.id WHERE p.institutionId = ?1""")
    List<ProjectDTO> findProjectsByInstitutionIdOrUserId(UUID institutionId, UUID userId);

    // UPDATE PROJECT
    @Modifying
    @Transactional
    @Query("""
            UPDATE Project p SET p.name = ?1, p.description = ?2, p.startDate = ?3, p.endDate = ?4, p.isActive = ?5, p.institutionId = ?6, p.userId = ?7, p.updatedAt = ?8 WHERE p.id = ?9""")
    int updateProject(String name, String description, LocalDate startDate, LocalDate endDate, boolean isActive, UUID institutionId, UUID userId, LocalDateTime updatedAt, UUID id);

    // DELETE PROJECT
    @Modifying
    @Transactional
    @Query("""
            DELETE FROM Project p WHERE p.id = ?1""")
    int deleteProject(UUID id);
}
