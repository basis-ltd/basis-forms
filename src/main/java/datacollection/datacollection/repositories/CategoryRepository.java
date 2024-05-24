package datacollection.datacollection.repositories;

import datacollection.datacollection.dtos.CategoryDTO;
import datacollection.datacollection.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Category findByName(String name);

    Category findCategoryById(UUID id);

}
