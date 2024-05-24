package datacollection.datacollection.repositories;

import datacollection.datacollection.entities.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {

    Form findByName(String name);
}
