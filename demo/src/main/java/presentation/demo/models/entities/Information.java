package presentation.demo.models.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Table(name = "information")
public class Information extends BaseEntity{

    private String type;
    private String body;
    private User author;
    private LocalDateTime leftOn;

    public Information() {
    }

    @Column(name = "type")
    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "body",columnDefinition = "TEXT")
    @NotNull
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ManyToOne
    @JoinColumn(name = "author")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "left_on")
    @PastOrPresent
    public LocalDateTime getLeftOn() {
        return leftOn;
    }

    public void setLeftOn(LocalDateTime leftOn) {
        this.leftOn = leftOn;
    }
}
