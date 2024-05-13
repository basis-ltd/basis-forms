package datacollection.datacollection.repositories;

import datacollection.datacollection.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByName(String name);
    Category findByDescription(String description);
    List<Category> findAllBy();
}