package presentation.demo.models.bindmodels;

public class MessageSendModel {

    private String receive;
    private String mess;
    private String sendfrom;

    public MessageSendModel() {
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getSendfrom() {
        return sendfrom;
    }

    public void setSendfrom(String sendfrom) {
        this.sendfrom = sendfrom;
    }
}
