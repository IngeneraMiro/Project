package presentation.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import presentation.demo.models.bindmodels.UserBindModel;
import presentation.demo.models.entities.User;
import presentation.demo.services.PracticeService;
import presentation.demo.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static presentation.demo.global.GlobalConstants.DOC_REGISTER_ERROR;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final PracticeService practiceService;

    @Autowired
    public UsersController(UserService userService, PracticeService practiceService) {
        this.userService = userService;
        this.practiceService = practiceService;
    }


    @GetMapping("/register")
    public String register(Model model, HttpSession session, @AuthenticationPrincipal Principal principal,
                           @RequestParam(value = "pName", required = false) String pName) {

        //  *** add bindModel to view model if not exist  ***
        if (!model.containsAttribute("userBindModel")) {
            model.addAttribute("userBindModel", new UserBindModel());
            model.addAttribute("userExist", false);
            model.addAttribute("passwornotdmatch", false);
        }
        model.addAttribute("user", principal);
        if (pName != null) {
            model.addAttribute("pName", pName);
        } else {
            model.addAttribute("pName", "empty");
        }
//  ***  add to view model all active practices to choose from  ***
        if (pName == null) {
            model.addAttribute("practices", this.practiceService.getAllActivePractice());
        } else {
            model.addAttribute("practices", Collections.singletonList(pName));
        }
        model.addAttribute("doctors", this.userService.getActiveDoctorsByPractice(pName));
        model.addAttribute("action", "pat");
        return "user-register";
    }

    @PostMapping("/register")
    public String regConform(@Valid @ModelAttribute("userBindModel") UserBindModel userBindModel,
                             BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        List<String> authorities = new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        System.out.println(authorities.get(0));
        if (result.hasErrors() || !userBindModel.getPassword().equals(userBindModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userBindModel", userBindModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userBindModel", result);
            if (!userBindModel.getPassword().equals(userBindModel.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("passwornotdmatch", true);
            }
            return "redirect:register?pName=" + URLEncoder.encode(userBindModel.getPractice(), StandardCharsets.UTF_8);
        }
        userBindModel.setAuthority("ROLE_PATIENT");
        try {
            User user = this.userService.addUser(userBindModel);
            switch (authorities.get(0)) {
                case "ROLE_ADMIN":
                    redirectAttributes.addFlashAttribute("regNumber", user.getUsername());
                    String message = String.format("Успешно регистрирахте пациент с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/practices/details?pName=" + URLEncoder.encode((String) session.getAttribute("pName"), StandardCharsets.UTF_8);
                case "ROLE_DOCTOR":
                    message = String.format("Успешно регистрирахте пациент с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/doctor/doctor-home";
                case "ROLE_NURSE":
                    message = String.format("Успешно регистрирахте пациент с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/nurse/nurse-home";
                case "ROLE_ANONYMOUS":
                    redirectAttributes.addFlashAttribute("regNumber", user.getUsername());
                    return "redirect:login";
                default:
                    return "redirect:/";
            }
        } catch (Error e) {
            return "redirect:register";
        }
    }


    @GetMapping("/registerdoc")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public String registerDoc(Model model, HttpSession session, @AuthenticationPrincipal Principal principal,
                              @RequestParam(value = "pName", required = false) String pName) {

        //  *** add bindModel to view model if not exist  ***
        if (!model.containsAttribute("userBindModel")) {
            model.addAttribute("userBindModel", new UserBindModel());
            model.addAttribute("userExist", false);
            model.addAttribute("passwornotdmatch", false);
        }
        model.addAttribute("user", principal);
        if (pName != null) {
            model.addAttribute("pName", pName);
        } else {
            model.addAttribute("pName", "empty");
        }
//  ***  add to view model all active practices to choose from  ***
        if (pName == null) {
            model.addAttribute("practices", this.practiceService.getAllActivePractice());
        } else {
            model.addAttribute("practices", Collections.singletonList(pName));
        }
        model.addAttribute("pName", pName);
        model.addAttribute("actiondoc", "doc");
        return "user-register";
    }

    @PostMapping("/registerdoc")
    @PreAuthorize("hasAnyRole('ADMIN','MAIN')")
    public String regDocConform(@Valid @ModelAttribute("userBindModel") UserBindModel userBindModel,
                                BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        List<String> authorities = new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        System.out.println(authorities.get(0));
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("userBindModel", userBindModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userBindModel", result);
            return "redirect:registerdoc";
        }
        userBindModel.setAuthority("ROLE_DOCTOR");
        try {
            User user = this.userService.addUser(userBindModel);
            switch (authorities.get(0)) {
                case "ROLE_ADMIN":
                    String message = String.format("Успешно регистрирахте лекар с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/practices/details?pName=" + URLEncoder.encode((String) session.getAttribute("pName"), StandardCharsets.UTF_8);
                case "ROLE_MAIN":
                    message = String.format("Успешно регистрирахте лекар с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/doctor/doctor-home";
                default:
                    return "redirect:/";
            }
        } catch (Error e) {
            return "redirect:registerDoc" + URLEncoder.encode(DOC_REGISTER_ERROR, StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/registernurse")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public String registerNurse(Model model, HttpSession session, @AuthenticationPrincipal Principal principal,
                                @RequestParam(value = "pName", required = false) String pName) {

        //  *** add bindModel to view model if not exist  ***
        if (!model.containsAttribute("userBindModel")) {
            model.addAttribute("userBindModel", new UserBindModel());
            model.addAttribute("userExist", false);
            model.addAttribute("passwornotdmatch", false);
        }
        model.addAttribute("user", principal);
        if (pName != null) {
            model.addAttribute("pName", pName);
        } else {
            model.addAttribute("pName", "empty");
        }
//  ***  add to view model all active practices to choose from  ***
        if (pName == null) {
            model.addAttribute("practices", this.practiceService.getAllActivePractice());
        } else {
            model.addAttribute("practices", Collections.singletonList(pName));
        }
        model.addAttribute("pName", pName);
        model.addAttribute("doctors", this.userService.getActiveDoctorsByPractice(pName));
        model.addAttribute("actionnurse", "nurse");
        return "user-register";
    }

    @PostMapping("/registernurse")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public String regNurseConform(@Valid @ModelAttribute("userBindModel") UserBindModel userBindModel,
                                  BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        List<String> authorities = new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        System.out.println(authorities.get(0));
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("userBindModel", userBindModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userBindModel", result);

            return "redirect:registernurse?pName=" + URLEncoder.encode(userBindModel.getPractice(), StandardCharsets.UTF_8);
        }
        userBindModel.setAuthority("ROLE_NURSE");
        try {
            User user = this.userService.addUser(userBindModel);
            switch (authorities.get(0)) {
                case "ROLE_ADMIN":
                    String message = String.format("Успешно регистрирахте сестра с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/practices/details?pName=" + URLEncoder.encode((String) session.getAttribute("pName"), StandardCharsets.UTF_8);
                case "ROLE_DOCTOR":
                case "ROLE_MAIN":
                    message = String.format("Успешно регистрирахте сестра с регистрационен номер %s", user.getUsername());
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/doctor/doctor-home";
                default:
                    return "redirect:/";
            }
        } catch (Error e) {
            return "redirect:registerDoc" + URLEncoder.encode(DOC_REGISTER_ERROR, StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session
            , @RequestParam(value = "message", required = false) String message, @RequestParam(value = "logout", required = false) String logout) {


        model.addAttribute("notExist", false);
        session.setAttribute("userType", "GUEST");
        session.setAttribute("pName", "empty");

//   ***  add messages (if any) to model  ***
        if (message != null) {
            model.addAttribute("notExist", true);
            model.addAttribute("message", message);
        }
        if (logout != null && logout.equals("true")) {
            model.addAttribute("logout", true);
        }

//  ***  add to view model all active practices to choose from  ***
        model.addAttribute("practices", this.practiceService.getAllActivePractice());
        return "/user-login";
    }

    @PostMapping("/login")
    public String logConfirm(RedirectAttributes redirectAttributes, HttpSession session) {

        return "redirect:login";
    }

    @GetMapping("role-change")
    @PreAuthorize("hasRole('ADMIN')")
    public String roleChange(Model model, @AuthenticationPrincipal Principal principal) {

        model.addAttribute("user", principal);
        return "role-change";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return "redirect:/";
    }

}
