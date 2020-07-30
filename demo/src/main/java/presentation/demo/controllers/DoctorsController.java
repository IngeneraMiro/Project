package presentation.demo.controllers;

import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.entities.User;
import presentation.demo.services.PracticeService;
import presentation.demo.services.UserService;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Controller
@RequestMapping("/doctor")
@PreAuthorize("hasAnyRole('DOCTOR','MAIN')")
public class DoctorsController {
    private final PracticeService practiceService;
    private final UserService userService;

    public DoctorsController(PracticeService practiceService, UserService userService) {
        this.practiceService = practiceService;
        this.userService = userService;
    }


    @GetMapping("/doctor-home")
    public String doctorHome(Model model, HttpSession session,
                             @AuthenticationPrincipal Principal principal) throws NotFoundException {
        User user = this.userService.getUserByRegNumber(principal.getName());
        Practice practice = user.getPractice();

        model.addAttribute("logout",true);
        model.addAttribute("number",user.getUsername());
        session.setAttribute("pName",practice.getName());
        session.setAttribute("family",user.getLastName());
        return "doctor-home";
    }

    @PostMapping("/add/{pName}")
    public String addUser(@RequestParam(value = "user",required = false)String user,
                          @PathVariable(value = "pName",required = false)String pName ){
        if(pName==null){
            return "redirect:/";
        }
        switch (user){
            case "doc":
                return "redirect:/users/registerdoc?pName="+ URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "nurse":
                return "redirect:/users/registernurse?pName="+URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case"pat":
                return "redirect:/users/register?pName="+URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "office":
                return "redirect:/offices/add?pName="+URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "del":
                return "redirect:/offices/del?pName="+URLEncoder.encode(pName, StandardCharsets.UTF_8);
            default:
                return "redirect:/";
        }
    }




}
