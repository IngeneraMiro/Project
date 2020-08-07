package presentation.demo.integration;

import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import presentation.demo.models.bindmodels.InformationBindModel;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InformationRestControllerTest {
    private InformationBindModel informationBindModel = new InformationBindModel("test","test information","A888888");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    Gson json;


//     if new Information is send from not existing user in service level protection
    @Order(1)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testNewInformationAddNotFound() throws Exception {
        this.informationBindModel.setAuthor("A884477");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/information/new")
                .content(json.toJson(informationBindModel,InformationBindModel.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Order(2)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testNewInformationAdd() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/information/new")
                .content(json.toJson(informationBindModel,InformationBindModel.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());
    }

//    cross site request forgery attack guard
    @Order(3)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testNewInformationAddForbidenCSRFExeption() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/information/new")
                .content(json.toJson(informationBindModel,InformationBindModel.class))
                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden());
    }

//    user that is not admin can't access rest controller method
    @Order(4)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_DOCTOR")
    public void testNewInformationAddNoAcceptableException() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/information/new")
                .content(json.toJson(informationBindModel,InformationBindModel.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNotAcceptable());
    }

//    user that is not admin can't save message at service level
    @Order(5)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testNewInformationAddNotPermissionException() throws Exception {
        informationBindModel.setAuthor("B888887");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/information/new")
                .content(json.toJson(informationBindModel,InformationBindModel.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Order(6)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testGetInformationByType() throws Exception {
        this.mockMvc.perform(get("/information/getinfo/test"))
                .andExpect(status().isOk());
    }

    @Order(7)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testDeleteInformation() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/information/delete/test")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Order(8)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testGetInformationByTypeNotFound() throws Exception {
        this.mockMvc.perform(get("/information/getinfo/test").with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Order(9)
    @Test
    @WithMockUser(username = "A888888", authorities = "ROLE_ADMIN")
    public void testGetLog() throws Exception {
        this.mockMvc.perform(get("/information/log").with(csrf())).andExpect(status().isOk());
    }
}
