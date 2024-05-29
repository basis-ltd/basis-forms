package datacollection.datacollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;


import static datacollection.datacollection.utils.PasswordEncoder.hashPassword;

// Assuming your packages are structured correctly
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
@RequestMapping(value = "/api")
public class DataCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCollectionApplication.class, args);
    }
}
