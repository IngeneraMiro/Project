package presentation.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import presentation.demo.services.PracticeService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final PracticeService practiceService;

    public HomeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }


    @GetMapping("/")
    public String index(Model model,@RequestParam(value = "logout",required = false)Boolean logout
            , HttpSession session,@AuthenticationPrincipal Principal principal){
        session.invalidate();
        model.addAttribute("user",principal);
        Boolean log = logout!=null ? logout : false;
        model.addAttribute("logout",log);
        List<String> practices = this.practiceService.getAllActivePractice();
        model.addAttribute("practices",practices);
        model.addAttribute("pName","empty");
        return "index";
    }

    @GetMapping("/test")
    public ModelAndView home(){
        System.out.println();
//        throw new RuntimeException("error");
//        throw new TransactionException("error");
//        throw new PersistenceException("error");
          throw new IllegalStateException("error");

    }

}
