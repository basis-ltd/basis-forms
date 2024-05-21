package datacollection.datacollection.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class PasswordEncoder {

    // HASH PASSWORD
    public static String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());
        return encoder.encode(password);
    }

    // COMPARE PASSWORDS
    public static boolean comparePasswords(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }
}
