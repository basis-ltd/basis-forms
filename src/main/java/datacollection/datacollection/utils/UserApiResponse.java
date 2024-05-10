package datacollection.datacollection.utils;

import datacollection.datacollection.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserApiResponse {

        public static class ObjectResponse {
            private String message;
            private User data;

            public ObjectResponse(String message, User data) {
                this.message = message;
                this.data = data;
            }
        }

        public static class ListResponse {
            private String message;
            private List<User> data;

            public ListResponse(String message, List<User> data) {
                this.message = message;
                this.data = data;
            }
        }
}
