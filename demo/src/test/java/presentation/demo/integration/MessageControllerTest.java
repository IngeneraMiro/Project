package presentation.demo.integration;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.repositories.MessageRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration
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
        this.mockMvc.perform(get("/info/single/ac1e4cf1-6848-4cc1-8e60-099294ccf9f3"))
                .andExpect(status().isOk());
    }

   @Test
    public void testSendNewMessageTrowForbiddenCsrf() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"D002707\",\"mess\":\"admin message\"}"))
                .andExpect(status().isForbidden());
   }

    @Test
    @WithMockUser(username = "A888888", roles = { "ADMIN" })
    public void testSendNewMessage() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"A888888\",\"mess\":\"admin message\"}"))
                .andExpect(status().isOk());
    }

}
