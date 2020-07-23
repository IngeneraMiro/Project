package presentation.demo.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "offices")
public class Office extends BaseEntity{

    private String address;
    private Practice practice;
    private String schedule;
    private String phone;

    public Office() {
    }

    @Column(name = "office_address",nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    @Column(name = "schedule")
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Column(name = "work_phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
