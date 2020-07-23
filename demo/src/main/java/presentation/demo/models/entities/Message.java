package presentation.demo.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity{

    private String body;
    private LocalDateTime leftAt;
    private User author;
    private User recipient;
    private Boolean wasRead;

    public Message() {
    }

    @Column(name = "message_body")
    @Length(max = 200)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(name = "left_at")
    @PastOrPresent
    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    @ManyToOne()
    @JoinColumn(name = "from_user")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "to_user")
    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @Column(name = "was_read",nullable = false)
    public Boolean getRead() {
        return wasRead;
    }

    public void setRead(Boolean read) {
        wasRead = read;
    }
}
