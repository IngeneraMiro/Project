package presentation.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import presentation.demo.configurations.CustomEventPublisher;
import presentation.demo.services.PracticeService;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Controller
@RequestMapping("admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PracticeService practiceService;
    private final CustomEventPublisher eventPublisher;

    public AdminController(PracticeService practiceService, CustomEventPublisher eventPublisher) {
        this.practiceService = practiceService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/admin-home")
    public String adminHome(Model model, HttpSession session, @AuthenticationPrincipal Principal principal) {
        this.eventPublisher.publishEvent("Admin logged!");
        model.addAttribute("user", principal);
        session.setAttribute("pName", "empty");
        model.addAttribute("practices", this.practiceService.getAllPractices());

        return "admin-home";
    }

    @PostMapping("/add/{pName}")
    public String addUser(@RequestParam(value = "user", required = false) String user,
                          @PathVariable(value = "pName", required = false) String pName) {
        if (pName == null) {
            return "redirect:/";
        }
        switch (user) {
            case "doc":
                return "redirect:/users/registerdoc?pName=" + URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "nurse":
                return "redirect:/users/registernurse?pName=" + URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "change":
                return "redirect:/users/role-change?pName=" + URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "office":
                return "redirect:/offices/add?pName=" + URLEncoder.encode(pName, StandardCharsets.UTF_8);
            case "delete":
                return "redirect:/offices/del?pName=" + URLEncoder.encode(pName, StandardCharsets.UTF_8);
            default:
                return "redirect:/";
        }

    }


    @GetMapping("/addinfo")
    public String infoAdding(){


        return "redirect:";
    }
}
