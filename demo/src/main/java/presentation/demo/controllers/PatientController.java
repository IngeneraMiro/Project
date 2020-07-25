package presentation.demo.controllers;

import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.models.viewmodels.UserViewModel;
import presentation.demo.services.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    private final UserService userService;

    public PatientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/patient-home")
    public String patientHome(Model model, @AuthenticationPrincipal Principal principal,
                              HttpSession session) throws NotFoundException {
        model.addAttribute("pName",principal.getName());
        UserControlViewModel uModel =  this.userService.getUserControlModel(principal.getName());
        session.setAttribute("name",uModel.getFirstName());
        session.setAttribute("doc",uModel.getDocName());
        session.setAttribute("nurse",uModel.getNurseName());
        session.setAttribute("nName",uModel.getNurseNum());
        session.setAttribute("practice",uModel.getPractice());
        return "patient-home";
    }

}
