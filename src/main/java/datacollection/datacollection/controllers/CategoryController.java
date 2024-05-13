package datacollection.datacollection.controllers;

import datacollection.datacollection.entities.Category;
import datacollection.datacollection.repositories.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // FETCH CATEGORIES
    @GetMapping(value = "")
    public List<Category> fetchCategories() {
        return categoryRepository.findAll();
    }
}
