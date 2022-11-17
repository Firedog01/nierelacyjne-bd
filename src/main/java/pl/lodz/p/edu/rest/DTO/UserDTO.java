package pl.lodz.p.edu.rest.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.lodz.p.edu.rest.model.users.User;

public class UserDTO {

    @NotNull
    @NotEmpty
    private String login;

    public UserDTO() {}

    public UserDTO(User user) {
        this.login = user.getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
