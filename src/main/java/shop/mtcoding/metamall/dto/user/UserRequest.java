package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {
    @Getter @Setter
    public static class LoginDto {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }
    @Getter @Setter
    public static class joinDto {
        @NotEmpty
        @Size(min = 3, max = 20)

        private String username;
        @NotEmpty
        @Size(min = 3, max = 20)
        private String password;
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 아닙니다")
        private String email;
        @NotEmpty
        @Pattern(regexp ="USER|ADMIN|SELLER")
        private String role;

    }
}
