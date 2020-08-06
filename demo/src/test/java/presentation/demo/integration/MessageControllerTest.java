package presentation.demo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.services.MessageService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {
    private MessageSendModel messageSendModel = new MessageSendModel("A888888","test message","A88888888");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MessageService messageService;


    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testSendNewMessage() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"A888888\",\"mess\":\"admin message\"}"))
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
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testSendNewMessageBadRequest() throws Exception {
        this.mockMvc.perform(put("/info/send")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receive\": \"A888888\", \"sendfrom\": \"E002707\",\"mess\":\"admin message\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testCountUnreadMessages() throws Exception {
        this.mockMvc.perform(get("/info/count/A888888")).
                andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testCountUnreadMessagesNotAcceptable() throws Exception {
        this.mockMvc.perform(get("/info/count/A888888")).
                andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testGetSingleMessageById() throws Exception {
        String id = this.messageService.getUnreadMessagesByUser("A888888").get(0).getId();
        String request = "/info/single/"+id;
        this.mockMvc.perform(get(request))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSingleMessageByIdNotAcceptable() throws Exception {
        this.mockMvc.perform(get("/info/single/ac1e4cf1-6848-4cc1-8e60-099294ccf9f3"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testGetSingleMessageByIdNotFound() throws Exception {
        this.mockMvc.perform(get("/info/single/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testDeleteAdminMessages() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/info/admin").with(csrf()))
                .andExpect(status().isOk());
    }

}
