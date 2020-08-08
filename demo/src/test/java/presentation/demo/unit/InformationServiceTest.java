package presentation.demo.unit;


import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import presentation.demo.models.bindmodels.InformationBindModel;
import presentation.demo.models.entities.Information;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.InformationViewModel;
import presentation.demo.repositories.InfoRepository;
import presentation.demo.services.UserService;
import presentation.demo.services.serviceImpl.InformationServiceImpl;

import javax.naming.NoPermissionException;
import java.time.LocalDateTime;

@SpringBootTest
public class InformationServiceTest {
    private static final String FIRST_NAME = "mirko";
    private static final String LAST_NAME = "dege";
    private static final String PRACTICE_NAME = "Витал ООД";

    private User testUser;
    private Information information;
    private InformationBindModel model;
    @Autowired
    ModelMapper mapper;
    @Mock
    InfoRepository infoRepository;
    @Mock
    UserService userService;
    @InjectMocks
    InformationServiceImpl informationService;

    @BeforeEach
    void setup(){
        this.informationService = new InformationServiceImpl(this.mapper,this.userService,this.infoRepository);

        testLoad();
    }


    @Test
    public void testSafeInformation() throws NotFoundException, NoPermissionException {
//    Arrenge
        Mockito.when(this.userService.getUserByRegNumber("A888888")).thenReturn(testUser);
        Mockito.when(this.infoRepository.saveAndFlush(Mockito.any(Information.class)))
                .thenReturn(information);
//    Act
        Information result = this.informationService.saveInfo(model);
//    Assert
        Assert.assertEquals(information,result);
    }

    @Test
    public void testSafeInformationNoPermissionException()  {
//    Arrenge
        String expected = "Нямате Права да записвате информация!";
        try {
            Mockito.when(this.userService.getUserByRegNumber("A888888")).thenReturn(testUser);
        Mockito.when(this.infoRepository.saveAndFlush(Mockito.any(Information.class)))
                .thenReturn(information);
//    Act/Assert
        model.setAuthor("N888888");
        this.informationService.saveInfo(model);
        } catch (NotFoundException | NoPermissionException e) {
            Assert.assertEquals(expected,e.getMessage());
        }
    }

    @Test
    public void testGetInformationByType() throws NotFoundException {
//    Arrange
        InformationViewModel exoected = new InformationViewModel();
        exoected.setBody("Test information unit!");
        Mockito.when(this.infoRepository.findByType("Test type!")).thenReturn(java.util.Optional.ofNullable(this.information));
//    Act
        InformationViewModel result = this.informationService.getInfoByType("Test type!");

//        Assert
        Assert.assertEquals(exoected.getBody(),result.getBody());
    }

    @Test
    public void testGetInformationByTypeNotFoundException() {
//    Arrange
        String expected = "";
        Mockito.when(this.infoRepository.findByType("Test type!")).thenReturn(java.util.Optional.ofNullable(this.information));
//    Act
        InformationViewModel result = null;
        try {
            result = this.informationService.getInfoByType("Test type!");
        } catch (NotFoundException e) {
            Assert.assertEquals(expected,result.getBody());
        }
    }


    void testLoad(){
        testUser = new User();
        testUser.setFirstName(FIRST_NAME);
        testUser.setLastName(LAST_NAME);
        testUser.setId("123456789abv");
        testUser.setPassword("123");
        testUser.setUsername("A888888");

        information = new Information();
        information.setLeftOn(LocalDateTime.now());
        information.setAuthor(testUser);
        information.setBody("Test information unit!");
        information.setType("Test type!");
        information.setId("firstInformation");

        model = new InformationBindModel();
        model.setAuthor("A888888");
        model.setBody("Test information unit!");
        model.setType("Test type!");
    }
}
