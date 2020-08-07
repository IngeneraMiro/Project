package presentation.demo.models.bindmodels;

public class InformationBindModel {

    private String type;
    private String body;
    private String author;

    public InformationBindModel() {
    }

    public InformationBindModel(String type, String body, String author) {
        this.type = type;
        this.body = body;
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
