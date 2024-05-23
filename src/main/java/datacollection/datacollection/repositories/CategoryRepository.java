package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.CategoryDTO;
import datacollection.datacollection.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT new datacollection.datacollection.dtos.CategoryDTO(" +
            "c.id, c.name, c.description, c.isActive, c.createdAt, c.updatedAt) " +
            "FROM Category c WHERE c.name = :name")
    CategoryDTO findByName(String name);
    Category findByDescription(String description);

    @Query("SELECT new datacollection.datacollection.dtos.CategoryDTO(" +
            "c.id, c.name, c.description, c.isActive, c.createdAt, c.updatedAt) " +
            "FROM Category c WHERE c.id = :id")
    CategoryDTO findCategoryById(UUID id);

    @Query("SELECT new datacollection.datacollection.dtos.CategoryDTO(" +
            "c.id, c.name, c.description, c.isActive, c.createdAt, c.updatedAt) " +
            "FROM Category c")
    List<CategoryDTO> findAllCategories();
}
