package presentation.demo.models.bindmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UserLogBindModel {

    private String username;
    private String password;

    public UserLogBindModel() {
    }

    @Length(min = 7,max = 8,message = "Номерът трябва да бъде точно 7  символа!")
    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(min = 3,max = 20,message = "Паролата трябва да е между 3 и 20 символа!")
    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
