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
import presentation.demo.models.bindmodels.UserBindModel;
import presentation.demo.models.entities.Authority;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.models.viewmodels.UserViewModel;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.repositories.InfoRepository;
import presentation.demo.repositories.UserRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.serviceImpl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private User doctor;
    private User nurse;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthorityRepository authorityRepository;
    @Mock
    InfoRepository infoRepository;
    @Mock
    PracticeService practiceService;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setup() {

        Authority authority = new Authority();
        authority.setAuthority("ROLE_ADMIN");
        authority.setId("firstAuthority");
        Authority secondAuthority = new Authority();
        secondAuthority.setId("secondAuthority");
        secondAuthority.setAuthority("ROLE_MAIN");
        Authority thirdAuthority = new Authority();
        thirdAuthority.setId("ThirdAuthority");
        thirdAuthority.setAuthority("ROLE_DOCTOR");
        Authority fourthAuthority = new Authority();
        fourthAuthority.setId("fourthAuthority");
        Mockito.when(authorityRepository.findByAuthority("ROLE_ADMIN")).thenReturn(authority);
        Mockito.when(authorityRepository.findByAuthority("ROLE_MAIN")).thenReturn(secondAuthority);
        Mockito.when(authorityRepository.findByAuthority("ROLE_DOCTOR")).thenReturn(thirdAuthority);
        Practice practice = new Practice();
        practice.setActive(true);
        practice.setName(PRACTICE_NAME);
        practice.setCreatedOn(LocalDateTime.now());
        practice.setLogo(PRACTICE_NAME);
        practice.setPhoneNumber("0897984545");
        practice.setRegNumber("GP8547fr74");
        practice.setId("firstPractice");
        Mockito.when(practiceService.getByName(PRACTICE_NAME)).thenReturn(practice);

        loadTestUsers();
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserServiceImpl(this.userRepository, this.authorityRepository, this.infoRepository, this.practiceService, encoder, mapper);

    }

    @Test
    public void testAddUser(){
//    Arrange
        User expected = testUser;
        UserBindModel model = this.mapper.map(testUser,UserBindModel.class);
        model.setConfirmPassword(model.getPassword());
        model.setDoctor(testUser.getDoctor().getLastName());
        model.setPractice(testUser.getPractice().getName());
        model.setAuthority(testUser.getAuthorities().iterator().next().getAuthority());
        Mockito.when(this.userRepository.getDoctorFromPractice(model.getDoctor(),PRACTICE_NAME)).thenReturn(doctor);
        Mockito.when(this.userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(testUser);
//    Act
        User result = this.userService.addUser(model);
//    Assert
        Assert.assertEquals(expected.getFirstName(),result.getFirstName());
        Assert.assertEquals(expected.getDoctor(),result.getDoctor());
        Assert.assertEquals(expected.getPractice(),result.getPractice());

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
            Assert.assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testBeginApplicationTrue() {
//    Arrange
        Mockito.when(this.userRepository.count()).thenReturn(0L);
//    Act
        boolean result = this.userService.begin();
//    Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testBeginApplicationFail() {
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
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetUserByRegNumberException() {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(testUser));
        String expected = "Потребител с регистрационен номер A888887 не беше намерен!";
//    Act/Assert
        try {
            this.userService.getUserByRegNumber("A888887");
        } catch (NotFoundException e) {
            Assert.assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testGetUserControlModel() throws NotFoundException {
//    Arrange
        Mockito.when(this.userRepository.findUserWithUsername("A888888")).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(this.userRepository.getNurseByDoc(doctor.getUsername())).thenReturn(nurse);
        UserControlViewModel model = this.mapper.map(testUser,UserControlViewModel.class);
        model.setDocName("Д-р "+ testUser.getDoctor().getLastName());
        model.setNurseName("сестра "+nurse.getLastName());
        model.setNurseNum(nurse.getUsername());
        model.setPractice(testUser.getPractice().getName());
//    Act
        UserControlViewModel result = this.userService.getUserControlModel("A888888");
//    Assert
        Assert.assertEquals(model.getDocName(),result.getDocName());
        Assert.assertEquals(model.getNurseName(),result.getNurseName());
        Assert.assertEquals(model.getPractice(),result.getPractice());
        Assert.assertEquals(model.getFirstName(),result.getFirstName());
        Assert.assertEquals(model.getLastName(),result.getLastName());
        Assert.assertEquals(model.getUsername(),result.getUsername());
    }

    @Test
    public void testGetActiveDoctorsByPractice(){
//    Arrange
        List<User> users = new ArrayList<>();
        users.add(testUser);
        List<UserViewModel> models = new ArrayList<>();
        UserViewModel model = this.mapper.map(testUser,UserViewModel.class);
        Set<String> authorities = testUser.getAuthorities().stream().map(a->a.getAuthority().substring(5)).collect(Collectors.toSet());
        model.setAuthorities(String.join(", ",authorities));
        models.add(model);
        Mockito.when(this.userRepository.getDoctorsByPractice(doctor.getPractice().getName())).thenReturn(users);
//    Act
        List<UserViewModel> result = this.userService.getActiveDoctorsByPractice(doctor.getPractice().getName());
//    Assert
        Assert.assertEquals(models.size(),result.size());
        Assert.assertEquals(models.get(0).getAuthorities(),result.get(0).getAuthorities());
        Assert.assertEquals(models.get(0).getFirstName(),result.get(0).getFirstName());
        Assert.assertEquals(models.get(0).getLastName(),result.get(0).getLastName());
        Assert.assertEquals(models.get(0).getUsername(),result.get(0).getUsername());
    }


    private void loadTestUsers(){
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
        nurse.addAuthority(this.authorityRepository.findByAuthority("ROLE_DOCTOR"));
        nurse.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        nurse.setPassword("123");
        nurse.setUsername("N936471");

        testUser = new User();
        testUser.setFirstName(FIRST_NAME);
        testUser.setLastName(LAST_NAME);
        testUser.setId("123456789abv");
        testUser.setDoctor(doctor);
        testUser.addAuthority(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
        testUser.setPractice(this.practiceService.getByName(PRACTICE_NAME));
        testUser.setPassword("123");
        testUser.setUsername("A888888");

    }
}


