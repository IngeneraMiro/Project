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
import presentation.demo.configurations.CustomEventPublisher;
import presentation.demo.customevents.MessageEvent;
import presentation.demo.models.bindmodels.MessageBindModel;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.models.entities.Authority;
import presentation.demo.models.entities.Message;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.MessageViewModel;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.repositories.MessageRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.UserService;
import presentation.demo.services.serviceImpl.MessageServiceImpl;

import javax.naming.NoPermissionException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MessageServiceTest {
    private static final String PRACTICE_NAME = "Витал ООД";

    private Message testMessage;
    private User doctor;
    private User nurse;
    private User sender;
    private  Practice practice;
    private MessageViewModel model;
    private MessageBindModel bindModel;
    private MessageSendModel sendModel;

    @Autowired
    ModelMapper mapper;
    @Autowired
    CustomEventPublisher eventPublisher;
    @Mock
    MessageRepository messageRepository;
    @Mock
    UserService userService;
    @Mock
    AuthorityRepository authorityRepository;
    @Mock
    PracticeService practiceService;
    @InjectMocks
    MessageServiceImpl messageService;

    @BeforeEach
    void setup(){
        Authority authority = new Authority();
        authority.setAuthority("ROLE_DOCTOR");
        authority.setId("firstAuthority");
        Authority secondAuthority = new Authority();
        secondAuthority.setId("secondAuthority");
        secondAuthority.setAuthority("ROLE_PATIENT");
        Authority thirdAuthority = new Authority();
        thirdAuthority.setId("thirdAuthority");
        thirdAuthority.setAuthority("ROLE_NURSE");
        Mockito.when(this.authorityRepository.findByAuthority("ROLE_DOCTOR")).thenReturn(authority);
        Mockito.when(this.authorityRepository.findByAuthority("ROLE_PATIENT")).thenReturn(secondAuthority);
        Mockito.when(this.authorityRepository.findByAuthority("ROLE_NURSE")).thenReturn(thirdAuthority);

        practice = new Practice();
        practice.setActive(true);
        practice.setName(PRACTICE_NAME);
        practice.setCreatedOn(LocalDateTime.now());
        practice.setLogo(PRACTICE_NAME);
        practice.setPhoneNumber("0897984545");
        practice.setRegNumber("GP8547fr74");
        practice.setId("firstPractice");
        Mockito.when(practiceService.getByName(PRACTICE_NAME)).thenReturn(practice);
        testLoad();
        this.messageService = new MessageServiceImpl(this.messageRepository,this.userService,this.mapper, this.eventPublisher);
    }

    @Test
    public void testCountMessagesByUser(){
//    Arrange
        Long expected = 5L;
        Mockito.when(this.messageRepository.countUnreadMessagesByUser(sender.getUsername())).thenReturn(5L);
//    Act
        Long result = this.messageService.countMessagesByUser(sender.getUsername());
//    Assert
        Assert.assertEquals(expected,result);
    }

    @Test
    public void testGetUnreadMessagesByUser(){
//    Arrange
        List<Message> list = new ArrayList<>();
        list.add(testMessage);
        List<MessageViewModel> expected = new ArrayList<>();
        expected.add(model);
        Mockito.when(this.messageRepository.getUnreadMessagesByUser(sender.getUsername())).thenReturn(list);
//    Act
        List<MessageViewModel> result = this.messageService.getUnreadMessagesByUser(sender.getUsername());
//    Assert
        Assert.assertEquals(expected.size(),result.size());
        Assert.assertEquals(expected.get(0).getBody(),result.get(0).getBody());
    }

    @Test
    public void testGetMessageById() throws NotFoundException {
//    Arrange
        MessageViewModel expected = model;
        Mockito.when(this.messageRepository.findById(testMessage.getId())).thenReturn(java.util.Optional.ofNullable(testMessage));
//    Act
        MessageViewModel result = this.messageService.getMessageById(testMessage.getId());
//    Assert
        Assert.assertEquals(expected.getBody(),result.getBody());
        Assert.assertEquals(expected.getLeftFrom(),result.getLeftFrom());
    }

    @Test
    public void testGetMessageByIdException(){
//    Arrange
        String expected = "Не намерихме това съобщение!";
        Mockito.when(this.messageRepository.findById(testMessage.getId())).thenReturn(java.util.Optional.ofNullable(testMessage));
//    Act/Arrange
        try {
            this.messageService.getMessageById("test");
        } catch (NotFoundException e) {
            Assert.assertEquals(expected,e.getMessage());
        }
    }

    @Test
    public void TestLeaveMessage() throws NotFoundException, NoPermissionException {
//    Arrange
        bindModel.setFname(nurse.getFirstName());
        bindModel.setTname(nurse.getLastName());
        Message expected = testMessage;
        Mockito.when(this.userService.getUserByRegNumber(sender.getUsername())).thenReturn(sender);
        Mockito.when(this.userService.getByNamesAndPractice(nurse.getFirstName()
                ,nurse.getLastName(),practice.getName())).thenReturn(nurse);
        Mockito.when(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).thenReturn(testMessage);
//    Act
        Message result = this.messageService.leaveMessage(bindModel);
//    Assert
        Assert.assertEquals(expected,result);
    }

    @Test
    public void TestLeaveMessageNoPermissionException() throws NotFoundException {
//    Arrange
        bindModel.setFname(doctor.getFirstName());
        bindModel.setTname(doctor.getLastName());
        String expected = "Можете да оставите съобщение на медицинската сестра!";
        Mockito.when(this.userService.getUserByRegNumber(sender.getUsername())).thenReturn(sender);
        Mockito.when(this.userService.getByNamesAndPractice(doctor.getFirstName()
                ,doctor.getLastName(),practice.getName())).thenReturn(doctor);
        Mockito.when(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).thenReturn(testMessage);
//    Act/Assert
        try {
            this.messageService.leaveMessage(bindModel);
        } catch (NoPermissionException e) {
            Assert.assertEquals(expected,e.getMessage());
        }
    }

    @Test
    public void TestLeaveMessageNotFound() throws NotFoundException, NoPermissionException {
//    Arrange
        bindModel.setFname(nurse.getFirstName());
        bindModel.setTname("Георгиева");
        String expected = "Не намерихме потребител с тези имена!";
        Mockito.when(this.userService.getUserByRegNumber(sender.getUsername())).thenReturn(sender);
        Mockito.when(this.userService.getByNamesAndPractice(nurse.getFirstName(),nurse.getLastName(),practice.getName())).thenReturn(nurse);
        Mockito.when(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).thenReturn(testMessage);
//    Act/Ассерт
        try {
            this.messageService.leaveMessage(bindModel);
        }catch (NotFoundException e){
            Assert.assertEquals(expected,e.getMessage());
        }

    }

    @Test
    public void testSendMessage() throws NotFoundException, NoPermissionException {
//    Arrange
        Message expected = testMessage;
        Mockito.when(this.userService.getUserByRegNumber(sender.getUsername())).thenReturn(sender);
        Mockito.when(this.userService.getUserByRegNumber(nurse.getUsername())).thenReturn(nurse);
        Mockito.when(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).thenReturn(testMessage);
//    Act
        Message result = this.messageService.sendMessage(sendModel);
//    Assert
        Assert.assertEquals(expected,result);
    }

    @Test
    public void testSendMessageNoPermissionException() throws NotFoundException {
//    Arrange
        String expected = "Може да изпратите съобщение на медицинската сестра!";
        sendModel.setReceive(doctor.getUsername());
        Mockito.when(this.userService.getUserByRegNumber(sender.getUsername())).thenReturn(sender);
        Mockito.when(this.userService.getUserByRegNumber(doctor.getUsername())).thenReturn(doctor);
        Mockito.when(this.messageRepository.saveAndFlush(Mockito.any(Message.class))).thenReturn(testMessage);
//    Act/Assert
        try {
            this.messageService.sendMessage(sendModel);
        } catch (NoPermissionException e) {
            Assert.assertEquals(expected,e.getMessage());
        }

    }

    @Test
    public void testClearAdminMessages() throws NotFoundException {
        Assert.assertTrue(this.messageService.clearAdminMessages());
    }

    @Test
    public void testClearOldMessages() throws NotFoundException {
        Assert.assertTrue(this.messageService.clearOldMessages());
    }

    @Test
    public void testClearUnreadOldMessages() throws NotFoundException {
        Assert.assertTrue(this.messageService.clearMessages());
    }

    void testLoad(){

        doctor = new User();
        doctor.setFirstName("Татяна");
        doctor.setLastName("Вековска");
        doctor.setId("123456789hgut");
        doctor.addAuthority(this.authorityRepository.findByAuthority("ROLE_DOCTOR"));
        doctor.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        doctor.setPassword("123");
        doctor.setUsername("D375820");

        nurse = new User();
        nurse.setFirstName("Оля");
        nurse.setLastName("Иванова");
        nurse.setId("123456789ufka");
        nurse.addAuthority(this.authorityRepository.findByAuthority("ROLE_NURSE"));
        nurse.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        nurse.setPassword("123");
        nurse.setUsername("N936471");

        sender = new User();
        sender.setFirstName("Иван");
        sender.setLastName("Иванов");
        sender.setId("123456789jgyr");
        sender.addAuthority(this.authorityRepository.findByAuthority("ROLE_PATIENT"));
        sender.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        sender.setPassword("123");
        sender.setUsername("P856378");

        testMessage = new Message();
        testMessage.setRead(false);
        testMessage.setAuthor(sender);
        testMessage.setBody("It is test message!");
        testMessage.setLeftAt(LocalDateTime.now());
        testMessage.setRecipient(nurse);
        testMessage.setId("firstMessage");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.model = new MessageViewModel();
        model.setId(testMessage.getId());
        model.setBody(testMessage.getBody());
        model.setLeftFrom(testMessage.getAuthor().getFirstName() + " " + testMessage.getAuthor().getLastName());
        model.setLeftOn(testMessage.getLeftAt().format(formatter));

        this.bindModel = new MessageBindModel();
        bindModel.setFname(nurse.getFirstName());
        bindModel.setTname(nurse.getLastName());
        bindModel.setSendfrom(sender.getUsername());
        bindModel.setMess("It is test message!");

        this.sendModel = new MessageSendModel();
        sendModel.setSendfrom(sender.getUsername());
        sendModel.setReceive(nurse.getUsername());
        sendModel.setMess("It is test message!");
    }
}
