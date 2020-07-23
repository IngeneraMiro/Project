package presentation.demo.models.bindmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static presentation.demo.global.GlobalConstants.*;

public class UserBindModel {
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String practice;
    private String doctor;
    private String email;
    private String authority;

    public UserBindModel() {
    }

    @Length(min = 3,max = 20,message = USER_LENGTH_ERROR)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Length(min = 3,max = 20,message = "Фамилията трябва да е между 3 и 20 символа!")
    @NotNull(message = NULL_ERROR)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Length(min = 3,max = 20,message = PASS_LENGTH_ERROR)
    @NotNull(message = NULL_ERROR)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotNull
    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Email(message = EMAIL_FORMAT_ERROR)
    @NotEmpty(message = EMPTY_ERROR)
    @NotNull(message = NULL_ERROR)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
