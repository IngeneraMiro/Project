package presentation.demo.services.serviceImpl;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import presentation.demo.models.bindmodels.UserBindModel;
import presentation.demo.models.entities.Authority;
import presentation.demo.models.entities.Information;
import presentation.demo.models.entities.User;
import presentation.demo.models.entities.UserAuthorities;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.models.viewmodels.UserViewModel;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.repositories.InfoRepository;
import presentation.demo.repositories.UserRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static presentation.demo.global.GlobalConstants.FILE_ADDRESS;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final InfoRepository infoRepository;
    private final PracticeService practiceService;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, InfoRepository infoRepository, PracticeService practiceService, PasswordEncoder encoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.infoRepository = infoRepository;
        this.practiceService = practiceService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

   @PostConstruct
    private void init() throws IOException {

        if(begin()){
            StringBuilder sb = new StringBuilder();
            for (UserAuthorities a: UserAuthorities.values()){
                this.authorityRepository.save(new Authority(String.format("ROLE_%s",a.name())));
            }
            User user = new User("miro","Stefanov","A888888",encoder.encode("password"));
            user.setEmail("miroslav@nabytky.com");
            user.setRegisteredOn(LocalDateTime.now());
            this.userRepository.save(user);
            user.addAuthority(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
            this.userRepository.save(user);

            Information information = new Information();
            information.setType("Профилактични прегледи");
            sb.append("Всеки един от вас има право на един профилактичен преглед годишно.При откриване на заболяване" +
                    ", ще ви насочим към допълнителна специализирана медицинска помощ и лечение.").append(System.lineSeparator())
                    .append("Децата от 2 до 6 годишна възраст се явяват на профилактични прегледи два пъти годишно, " +
                            "като се извършват и задължителните за възрастта ваксинации.").append(System.lineSeparator())
                    .append("Учениците от 7 до 18 годишна възраст подлежат на профилактичен преглед един път годишно." +
                            "Обикновенно преди началото на учебната година.").append(System.lineSeparator())
                    .append("Грижа на нашият персинал е да следим за честотата на профилактичните прегледи и предварително" +
                            " да ви уведомяваме за тях.Затова Ви молим да посещавате редовно вашата персонална страница за да получите актуална информация.");
            information.setBody(sb.toString());
            information.setAuthor(user);
            information.setLeftOn(LocalDateTime.now());
            this.infoRepository.saveAndFlush(information);

            information = new Information();
            information.setType("Диспансерна дейност");
            sb = new StringBuilder();
            sb.append("При регистриране на хронично заболяване ние ще ви диспансеризираме.Така през определен период ще следим хода" +
                    " на заболяването и неговото лечение.Не пропускайте при всяка визита да попитате за датата на следващото посещение.");
            information.setBody(sb.toString());
            information.setAuthor(user);
            information.setLeftOn(LocalDateTime.now());
            this.infoRepository.saveAndFlush(information);

            information = new Information();
            information.setType("Амбулаторна дейност");
            sb = new StringBuilder();
            sb.append("Ежедневно се извършват амбулаторни прегледи на остри и хронични заболявания и техното лечение." +
                    "При неоходимост издаваме необходимите медицински документи за временна нетрудоспособност," +
                    " насочваме Ви за допълнителни прегледи и изследвания при специалисти.");
            information.setBody(sb.toString());
            information.setAuthor(user);
            information.setLeftOn(LocalDateTime.now());
            this.infoRepository.saveAndFlush(information);

            File log = new File(FILE_ADDRESS);
        }
   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserWithUsername(username).orElseThrow(()-> new UsernameNotFoundException("Потребител с регистрационен номер "+username+" не беше намерен!") );
        return user;
    }

    @Override
    @Transactional
    public User addUser(UserBindModel model) {
        User user = this.mapper.map(model,User.class);
        Authority authority = this.authorityRepository.findByAuthority(model.getAuthority());
         user.addAuthority(authority);

        while (true){
            String regNum = model.getAuthority().charAt(5) + regNumberGeneration();
            if(!this.userRepository.existsByUsername(regNum)){
                user.setUsername(regNum);
                break;
            }
        }
        if(model.getPractice()!=null){
            user.setPractice(this.practiceService.getByName(model.getPractice()));
            if(model.getDoctor()!=null && !model.getDoctor().isEmpty()){
                User doc = this.userRepository.getDoctorFromPractice(model.getDoctor(),model.getPractice());
                user.setDoctor(doc);
            }
        }

        user.setPassword(encoder.encode(model.getPassword()));
        user.setRegisteredOn(LocalDateTime.now());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        if(model.getAuthority().charAt(5)=='D'){
            user.setIsDoctor(true);
        }else if(model.getAuthority().charAt(5)=='N'){
            user.setIsNurse(true);
        }else {
            user.setIsDoctor(false);
            user.setIsNurse(false);
        }

        return this.userRepository.saveAndFlush(user);
    }

    @Override
    public Boolean begin() {
        return this.userRepository.count()==0;
    }

    @Override
    public User getUserByRegNumber(String number) throws NotFoundException {
        User user = this.userRepository.findUserWithUsername(number).orElse(null);
        if(user==null){
            throw new NotFoundException("Потребител с регистрационен номер "+number+" не беше намерен!");
        }
        return user;
   }

    @Override
    public List<UserViewModel> getActiveDoctorsByPractice(String practice) {
        List<User> list = this.userRepository.getDoctorsByPractice(practice);
        List<UserViewModel> listToSend = new ArrayList<>();
        for(User u: list){
           UserViewModel model = this.mapper.map(u,UserViewModel.class);
           Set<String> authorities = u.getAuthorities().stream().map(a->a.getAuthority().substring(5)).collect(Collectors.toSet());
           model.setAuthorities(String.join(", ",authorities));
           listToSend.add(model);
        }
        return listToSend;
    }

    @Override
    public User addMainDoctor(String username) throws NotFoundException {
        User user = this.userRepository.findUserWithUsername(username).orElseThrow(()->new NotFoundException("Потребител с регистрационен номер "+username+" не беше намерен!"));
        user.addAuthority(this.authorityRepository.findByAuthority("ROLE_MAIN"));
        return this.userRepository.saveAndFlush(user);
    }

    @Override
    public User doNormalDoctor(String username) throws NotFoundException {
        User user = this.userRepository.findUserWithUsername(username).orElseThrow(()->new NotFoundException("Потребител с регистрационен номер "+username+" не беше намерен!"));
        Set<Authority> authorities = user.getAuthorities();
        authorities.remove(this.authorityRepository.findByAuthority("ROLE_MAIN"));
        user.setAuthorities(authorities);
        return this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserControlViewModel getUserControlModel(String username) throws NotFoundException {
      User user = this.userRepository.findUserWithUsername(username).orElseThrow(()->new NotFoundException("Потребител с регистрационен номер "+username+" не беше намерен!"));
      UserControlViewModel model = this.mapper.map(user,UserControlViewModel.class);
      model.setDocName("Д-р "+ user.getDoctor().getLastName());
      User nurse = this.userRepository.getNurseByDoc(user.getDoctor().getUsername());
      model.setNurseName("сестра "+nurse.getLastName());
      model.setNurseNum(nurse.getUsername());
      model.setPractice(user.getPractice().getName());
      return model;
    }


    @Override
    public User getByNamesAndPractice(String firstName, String lastName, String pName) throws NotFoundException {
        User user = this.userRepository.findByNamesAndPractice(firstName,lastName,pName).orElseThrow(()->new NotFoundException("Не намерихме потребител с тези имена!"));
        return user;
    }


    //  Generate random registration number
    private String regNumberGeneration(){
        String pass = "";
        for (int i = 0; i < 6; i++) {
            int digit =  (int) (Math.random() * 9);
            pass+=digit;
        }
        return pass;
    }

}
