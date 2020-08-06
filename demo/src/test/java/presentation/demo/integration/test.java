package presentation.demo.integration;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.repositories.MessageRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
public class test {
    private MessageSendModel messageSendModel;

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    Gson json;

    @BeforeEach
    public void Setup(){

        messageSendModel = new MessageSendModel();
        this.messageSendModel.setMess("message");
        this.messageSendModel.setReceive("A888888");
        this.messageSendModel.setSendfrom("D002707");
    }


}
