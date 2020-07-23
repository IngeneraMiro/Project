package presentation.demo.restcontrollers;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.UserViewModel;
import presentation.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/info")
@PreAuthorize("hasAnyRole('ADMIN','MAIN')")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/doctors/{pName}")
    public List<UserViewModel> getUser(@PathVariable(value = "pName")String pName){
        List<UserViewModel> list = this.userService.getActiveDoctorsByPractice(pName);
        return list;
    }

    @PutMapping("/main/{username}")
    public ResponseEntity<?> makeMainDoc(@PathVariable(value = "username")String username) throws NotFoundException {
       User user =  this.userService.addMainDoctor(username);
        return ResponseEntity.ok("Доктор "+user.getLastName()+" вече е главен лекар!");
    }

    @PutMapping("/normal/{username}")
    public ResponseEntity<?> makeNormalDoc(@PathVariable(value = "username")String username) throws NotFoundException {
        User user =  this.userService.doNormalDoctor(username);
        return ResponseEntity.ok("Доктор "+user.getLastName()+" вече не е главен лекар!");
    }
}



