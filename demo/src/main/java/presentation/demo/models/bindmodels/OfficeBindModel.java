package presentation.demo.models.bindmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class OfficeBindModel {

    private String address;
    private String practice;
    private String schedule;
    private String phone;

    public OfficeBindModel() {
    }

    @NotNull
    @Length(min = 5, message = "Адресът на офиса не може да е по-малко от 5 символа!")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull
    @Length(min = 3,message = "Името на практиката не може да е по малко от 3 символа!")
    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    @NotNull
    @Length(min = 5,message = "Графикът не може да е по-малко от 5 символа!")
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @NotNull
    @Length(min = 7,max = 11,message = "Номерът трябва да бъде между 7 и 10 символа!")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
