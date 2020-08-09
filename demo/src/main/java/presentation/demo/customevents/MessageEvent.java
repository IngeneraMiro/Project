package presentation.demo.customevents;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    private String message;
    private String sender;

    public MessageEvent(Object source, String message, String sender) {
        super(source);
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
