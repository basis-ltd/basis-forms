package datacollection.datacollection.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StringsUtils {

    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (characters.length() * Math.random());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    public Object mapIdToObject(UUID id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    public LocalDateTime createDateFromString(String date) {
        return LocalDateTime.parse(date);
    }
}
