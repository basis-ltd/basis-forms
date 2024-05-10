package datacollection.datacollection.repositories;

import datacollection.datacollection.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID>{
    Institution findByName(String name);
    Institution findByEmail(String email);
}
