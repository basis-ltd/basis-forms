package datacollection.datacollection.constants;

import lombok.Getter;

@Getter
public enum FormVisibilities {
    PUBLIC("public"),
    PRIVATE("private");

    private final String visibility;

    FormVisibilities(String visibility) {
        this.visibility = visibility;
    }
}
