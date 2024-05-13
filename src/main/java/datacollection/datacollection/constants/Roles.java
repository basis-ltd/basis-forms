package datacollection.datacollection.constants;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("admin"),
    USER("user");

    // Getter method
    private final String roleName;

    // CONSTRUCTOR
    Roles(String roleName) {
        this.roleName = roleName;
    }

}
