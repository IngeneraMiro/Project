package presentation.demo.models.bindmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PracticeBindModel {

    private String name;
    private String regNumber;
    private String phoneNumber;
    private String logo;

    public PracticeBindModel() {
    }

    @Length(min = 3,max = 20,message = "Името трябва да бъде между 3 и 20 символа!")
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 3,max = 20,message = "Номерът трябва да бъде между 3 и 20 символа!")
    @NotNull
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Length(min = 7,max = 11,message = "Номерът трябва да бъде между 7 и 10 символа!")
    @NotNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @NotNull
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
