package datacollection.datacollection.repositories;

import datacollection.datacollection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{
    User findByEmail(String email);
    List<User> findByInstitutionId(UUID institutionId);
    List<User> findByRole(String role);
}
