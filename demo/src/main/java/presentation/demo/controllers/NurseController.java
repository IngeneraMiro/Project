package presentation.demo.controllers;

import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.services.UserService;

import java.security.Principal;

@Controller
@PreAuthorize("hasRole('NURSE')")
@RequestMapping("/nurse")
public class NurseController {
    private final UserService userService;

    public NurseController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/nurse-home")
    public String nurseHome(Model model, @AuthenticationPrincipal Principal principal) throws NotFoundException {
        UserControlViewModel model1 = this.userService.getUserControlModel(principal.getName());
        model.addAttribute("nurse",model1);
        model.addAttribute("pName",principal.getName());

        return "nurse-home";
    }

    @GetMapping("/add/{pName}")
    public String addUser(@PathVariable(value = "pName")String pName){


        return "";
    }


}
