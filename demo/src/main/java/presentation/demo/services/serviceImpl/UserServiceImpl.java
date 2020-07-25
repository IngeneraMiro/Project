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
import presentation.demo.models.entities.User;
import presentation.demo.models.entities.UserAuthorities;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.models.viewmodels.UserViewModel;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.repositories.UserRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PracticeService practiceService;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PracticeService practiceService, PasswordEncoder encoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.practiceService = practiceService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

   @PostConstruct
    private void init(){
        if(this.authorityRepository.count()==0){
            for (UserAuthorities a: UserAuthorities.values()){
                this.authorityRepository.save(new Authority(String.format("ROLE_%s",a.name())));
            }
            User user = new User("miro","Stefanov","A888888",encoder.encode("password"));
            user.setEmail("miroslav@nabytky.com");
            user.setRegisteredOn(LocalDateTime.now());
            this.userRepository.save(user);
            user.addAuthority(this.authorityRepository.findByAuthority("ROLE_ADMIN"));
            this.userRepository.save(user);
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
//        return this.userRepository.findByUsername(number).orElseThrow(()->new UsernameNotFoundException("Потребител с регистрационен номер "+number+" не беше намерен!"));
    }

    @Override
    public UserViewModel getUserByUsername(String username) {
        User user = this.userRepository.findUserWithUsername(username).orElse(null);
        return this.mapper.map(user,UserViewModel.class);
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
