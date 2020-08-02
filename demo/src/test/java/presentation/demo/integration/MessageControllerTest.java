package presentation.demo.integration;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.repositories.MessageRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {
    private MessageSendModel messageSendModel;

    @Autowired
    MockMvc mockMvc;
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

    @Test
    public void testCountUnreadMessages() throws Exception {
        this.mockMvc.perform(get("/info/count/A888888")).
                andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testGetSingleMessageById() throws Exception {
        this.mockMvc.perform(get("/info/single/4069e11f-ff03-4eb9-804e-cd4f2664eea0")).
                andExpect(status().isOk());
    }

   @Test
    public void testSendNewMessageTrowForbiddenCsrf() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"D002707\",\"mess\":\"admin message\"}"))
                .andExpect(status().isForbidden());
   }

    @Test
    public void testSendNewMessage() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"D002707\",\"mess\":\"admin message\"}"))
                .andExpect(status().isOk());
    }

}
