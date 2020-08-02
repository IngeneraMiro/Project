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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import presentation.demo.models.entities.Authority;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.entities.User;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.repositories.UserRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.serviceImpl.UserServiceImpl;

import java.time.LocalDateTime;

@SpringBootTest
public class UserServiceTest {
    private static final String FIRST_NAME = "mirko";
    private static final String LAST_NAME = "dege";
    private static final String PRACTICE_NAME = "Витал ООД";

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    ModelMapper mapper;
    private User testUser;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthorityRepository authorityRepository;
    @Mock
    PracticeService practiceService;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setup() {
        this.practiceService = Mockito.mock(PracticeService.class);
        this.authorityRepository = Mockito.mock(AuthorityRepository.class);
        Authority authority = new Authority();
        authority.setAuthority("ROLE_ADMIN");
        authority.setId("firstAuthority");
        Authority secondAuthority = new Authority();
        secondAuthority.setId("secondAuthority");
        secondAuthority.setAuthority("ROLE_MAIN");
        Mockito.when(authorityRepository.findByAuthority("ROLE_ADMIN")).thenReturn(authority);
        Mockito.when(authorityRepository.findByAuthority("ROLE_MAIN")).thenReturn(secondAuthority);
        Practice practice = new Practice();
        practice.setActive(true);
        practice.setName(PRACTICE_NAME);
        practice.setCreatedOn(LocalDateTime.now());
        practice.setLogo(PRACTICE_NAME);
        practice.setPhoneNumber("0897984545");
        practice.setRegNumber("GP8547fr74");
        practice.setId("firstPractice");
        Mockito.when(practiceService.getByName(PRACTICE_NAME)).thenReturn(practice);

        testUser = new User();
        testUser.setFirstName(FIRST_NAME);
        testUser.setLastName(LAST_NAME);
        testUser.setId("123456789abv");
        testUser.addAuthority(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
        testUser.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        testUser.setPassword("123");
        testUser.setUsername("A888888");
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserServiceImpl(this.userRepository, this.authorityRepository, this.practiceService, encoder, mapper);

    }


    @Test
    public void checkLoadUserByUserName() {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(this.testUser));
        User expected = testUser;

//    Act
        User result = (User) this.userService.loadUserByUsername("A888888");
//    Assert
        Assert.assertEquals(expected, result);
    }


    @Test
    public void testUserNotFoundLoadByUsername() {
//    Arrange
        String expected = "Потребител с регистрационен номер A888882 не беше намерен!";
//    Act/Assert
        try {
            this.userService.loadUserByUsername("A888882");
        } catch (UsernameNotFoundException ex) {
            Assert.assertEquals(expected, ex.getMessage());
        }
    }

    @Test
    public void checkUserRoleAdd() throws NotFoundException {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(this.testUser));
        Mockito.when(this.userRepository.saveAndFlush(testUser)).thenReturn(testUser);
//    Act
        this.userService.addMainDoctor("A888888");
//    Assert
        Assert.assertEquals(2L, testUser.getAuthorities().size());
    }

    @Test
    public void testDeleteRoleFromUser() throws NotFoundException {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(this.testUser));
        Mockito.when(this.userRepository.saveAndFlush(testUser)).thenReturn(testUser);
//    Act
        this.userService.doNormalDoctor("A888888");
//    Assert
        Assert.assertEquals(1L, testUser.getAuthorities().size());
    }

    @Test
    public void testGetUserByFirstAndLastNameAdnPractice() throws NotFoundException {
//    Arrange
        Mockito.when(this.userRepository.findByNamesAndPractice(FIRST_NAME, LAST_NAME, PRACTICE_NAME)).thenReturn(java.util.Optional.ofNullable(testUser));
        User expected = testUser;
//    Act
        User result = this.userService.getByNamesAndPractice(FIRST_NAME, LAST_NAME, PRACTICE_NAME);
//    Assert
        Assert.assertEquals(expected, result);

    }

    @Test
    public void testGetUserByNamesAndPracticeException() {
//    Arrange
        Mockito.when(this.userRepository.findByNamesAndPractice(FIRST_NAME, LAST_NAME, PRACTICE_NAME)).thenReturn(java.util.Optional.ofNullable(testUser));
        String expected = "Не намерихме потребител с тези имена!";
//    Act/Assert
        try {
            this.userService.getByNamesAndPractice("pesho", LAST_NAME, PRACTICE_NAME);
        } catch (NotFoundException e) {
           Assert.assertEquals(expected,e.getMessage());
        }
    }

    @Test
    public void testBeginApplicationTrue(){
//    Arrange
        Mockito.when(this.userRepository.count()).thenReturn(0L);
//    Act
        boolean result = this.userService.begin();
//    Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testBeginApplicationFail(){
//    Arrange
        Mockito.when(this.userRepository.count()).thenReturn(1L);
//    Act
        boolean result = this.userService.begin();
//    Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testGetUserByRegNumber() throws NotFoundException {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(testUser));
        User expected = testUser;
//    Act
        User result = this.userService.getUserByRegNumber("A888888");
        //    Assert
        Assert.assertEquals(expected,result);
    }

    @Test
    public void testGetUserByRegNumberException(){
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(testUser));
        String expected = "Потребител с регистрационен номер A888887 не беше намерен!";
//    Act/Assert
        try {
            this.userService.getUserByRegNumber("A888887");
        } catch (NotFoundException e) {
            Assert.assertEquals(expected,e.getMessage());
        }
    }



}


