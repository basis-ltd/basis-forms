package datacollection.datacollection;

import datacollection.datacollection.entities.Category;
import datacollection.datacollection.entities.Institution;
import datacollection.datacollection.entities.User;
import datacollection.datacollection.repositories.CategoryRepository;
import datacollection.datacollection.repositories.InstitutionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import datacollection.datacollection.repositories.UserRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import static datacollection.datacollection.utils.Security.hashPassword;

// Assuming your packages are structured correctly
@SpringBootApplication
@EnableJpaAuditing
@RequestMapping(value = "/api")
public class DataCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCollectionApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, InstitutionRepository institutionRepository, CategoryRepository categoryRepository) {
        return args -> {
            try {
                categoryRepository.deleteAll();
                institutionRepository.deleteAll();
                userRepository.deleteAll();
                User user = new User();
                Institution institution = new Institution();
                Category category = new Category();
                category.setName("Category");
                category.setDescription("Description");
                categoryRepository.save(category);
                institution.setName("University");
                institution.setAddress("123 Main St");
                institution.setEmail("user@email.rw");
                institution.setCategoryId(category.getId());
                institutionRepository.save(institution);
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setEmail("princeelysee@gmail.com");
                user.setPassword(hashPassword("password"));
                user.setRole("admin");
                user.setPhone("1234567890");
                user.setInstitutionId(institution.getId());
                userRepository.save(user);
                System.out.println(userRepository.findAll());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }
}
