package presentation.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import presentation.demo.models.bindmodels.OfficeBindModel;
import presentation.demo.models.entities.Office;
import presentation.demo.services.OfficeService;
import presentation.demo.services.PracticeService;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static presentation.demo.global.GlobalConstants.OFFICE_REGISTER_ERROR;


@Controller
@RequestMapping("offices")
@PreAuthorize("hasAnyRole('ADMIN','MAIN')")
public class OfficeController {

    private final OfficeService officeService;
    private final PracticeService practiceService;

    public OfficeController(OfficeService officeService, PracticeService practiceService) {
        this.officeService = officeService;
        this.practiceService = practiceService;
    }

    @GetMapping("/add")
    public String addOffice(Model model,@RequestParam(value = "pName")String pName, @AuthenticationPrincipal Principal principal){

        if(!model.containsAttribute("officeBindModel")){
            model.addAttribute("officeBindModel",new OfficeBindModel());
        }
        model.addAttribute("user",principal);
        model.addAttribute("pName",pName);
        model.addAttribute("practices", Collections.singletonList(pName));
        return "office-add";
    }

    @PostMapping("/add")
    public String addOfficeConfirm(@Valid @ModelAttribute("officeBindModel")OfficeBindModel officeBindModel,
                                   BindingResult result, RedirectAttributes redirectAttributes){
        List<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).distinct().collect(Collectors.toList());

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.officeBindModel", result);
            redirectAttributes.addFlashAttribute("officeBindModel",officeBindModel);
            return "redirect:add";
        }
        try{
            Office office = this.officeService.addNewOffice(officeBindModel);
            switch (authorities.get(0)) {
                case "ROLE_ADMIN":
                    String message = String.format("Успешно регистрирахте кабинет на адрес: %s",office.getAddress());
                    redirectAttributes.addFlashAttribute("message",message);
                    return "redirect:/practices/details?pName="+ URLEncoder.encode(officeBindModel.getPractice(), StandardCharsets.UTF_8);
                case "ROLE_DOCTOR":
                    message = String.format("Успешно регистрирахте лекар с регистрационен номер %s",office.getAddress());
                    redirectAttributes.addFlashAttribute("message",message);
                    return "redirect:/doctor/doctor-home";
                default:
                    return "redirect:/";
            }
        }catch (Error e){
            return "redirect:registerDoc"+URLEncoder.encode(OFFICE_REGISTER_ERROR, StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/del")
    public String dellOffice(Model model,@RequestParam(value = "pName")String pName,
                             @AuthenticationPrincipal Principal principal){
        model.addAttribute("pName",pName);
        model.addAttribute("user",principal);
        model.addAttribute("practice",this.practiceService.getPracticeByName(pName));

        return "office-delete";
    }

}
