package datacollection.datacollection.controllers;

import datacollection.datacollection.dtos.CategoryDTO;
import datacollection.datacollection.entities.Category;
import datacollection.datacollection.repositories.CategoryRepository;
import datacollection.datacollection.utils.ApiResponse;
import datacollection.datacollection.utils.StringsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final StringsUtils stringsUtils = new StringsUtils();

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // FETCH CATEGORIES
    @GetMapping(value = "")
    public ResponseEntity<Object> fetchCategories() {
        try {
            ApiResponse<List<CategoryDTO>> allCategories = new ApiResponse<>("Categories found successfully", categoryRepository.findAllCategories());
            return ResponseEntity.status(200).body(allCategories);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // GET CATEGORY BY ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> fetchCategoryById(@PathVariable UUID id) {
        try {
            CategoryDTO categoryExists = categoryRepository.findCategoryById(id);
            if (categoryExists == null) {
                ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                return ResponseEntity.status(404).body(categoryNotFound);
            }
            ApiResponse<Object> category = new ApiResponse<>("Category found successfully", categoryExists);
            return ResponseEntity.status(200).body(category);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // CREATE CATEGORY
    @PostMapping(value = "")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryDTO category) {
        try {
            CategoryDTO categoryExists = categoryRepository.findByName(category.getName());
            if (categoryExists != null) {
                System.out.println(stringsUtils.mapIdToObject(categoryExists.getId()));
                ApiResponse<Object> categoryConflict = new ApiResponse<>("Category already exists", stringsUtils.mapIdToObject(categoryExists.getId()));
                return ResponseEntity.status(409).body(categoryConflict);
            }
            Category newCategory = new Category();
            newCategory.setName(category.getName());
            newCategory.setDescription(category.getDescription());
            categoryRepository.save(newCategory);
            ApiResponse<Object> categoryResponse = new ApiResponse<>("Category created successfully", newCategory);
            return ResponseEntity.status(201).body(categoryResponse);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // DELETE CATEGORY
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable UUID id) {
        try {
            CategoryDTO categoryExists = categoryRepository.findCategoryById(id);
            if (categoryExists == null) {
                ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                return ResponseEntity.status(404).body(categoryNotFound);
            }
            categoryRepository.deleteById(id);
            ApiResponse<Object> categoryDeleted = new ApiResponse<>("Category deleted successfully", null);
            return ResponseEntity.status(200).body(categoryDeleted);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }

    // UPDATE CATEGORY
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO category) {
        try {
            CategoryDTO categoryExists = categoryRepository.findCategoryById(id);
            if (categoryExists == null) {
                ApiResponse<Object> categoryNotFound = new ApiResponse<>("Category not found", null);
                return ResponseEntity.status(404).body(categoryNotFound);
            }
            Category updatedCategory = new Category();
            updatedCategory.setId(id);
            updatedCategory.setName(category.getName());
            updatedCategory.setDescription(category.getDescription());
            categoryRepository.save(updatedCategory);
            ApiResponse<Object> categoryUpdated = new ApiResponse<>("Category updated successfully", updatedCategory);
            return ResponseEntity.status(200).body(categoryUpdated);
        } catch (Exception e) {
            ApiResponse<Object> responseBadRequest = new ApiResponse<>(e.getMessage(), null);
            return ResponseEntity.status(500).body(responseBadRequest);
        }
    }
}
