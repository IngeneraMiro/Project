package presentation.demo.models.viewmodels;

public class MessageViewModel {
    private String id;
    private String leftFrom;
    private String body;
    private String leftOn;

    public MessageViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeftFrom() {
        return leftFrom;
    }

    public void setLeftFrom(String leftFrom) {
        this.leftFrom = leftFrom;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLeftOn() {
        return leftOn;
    }

    public void setLeftOn(String leftOn) {
        this.leftOn = leftOn;
    }
}
