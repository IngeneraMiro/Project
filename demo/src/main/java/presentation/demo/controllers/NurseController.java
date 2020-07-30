package presentation.demo.controllers;

import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.services.UserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @PostMapping("/add/{pName}")
    public String addUser(@RequestParam(value = "user",required = false)String user,
                          @PathVariable(value = "pName",required = false)String pName ){

        if(user.equals("pat")){
            return "redirect:/users/register?pName="+ URLEncoder.encode(pName, StandardCharsets.UTF_8);
        }

        return "redirect:/";
    }


}
