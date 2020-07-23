package presentation.demo.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import presentation.demo.models.bindmodels.PracticeBindModel;
import presentation.demo.models.viewmodels.PracticeDetailsModel;
import presentation.demo.models.viewmodels.PracticeEditModel;
import presentation.demo.models.viewmodels.PracticeViewModel;
import presentation.demo.services.PracticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Controller
@RequestMapping("/practices")
public class PracticesController {
    private final PracticeService practiceService;
    private final ModelMapper mapper;

    @Autowired
    public PracticesController(PracticeService practiceService, ModelMapper mapper) {
        this.practiceService = practiceService;
        this.mapper = mapper;
    }


    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addPractice(Model model,@AuthenticationPrincipal Principal principal){
        if(!model.containsAttribute("practiceBindModel")){
            model.addAttribute("practiceBindModel",new PracticeBindModel());
        }
        model.addAttribute("pName","empty");
        model.addAttribute("user",principal);
        model.addAttribute("practices",practiceService.getAllPractices());
        return "practice-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addPracticeConfirm(@Valid @ModelAttribute("practiceBindModel")PracticeBindModel practiceBindModel,
                                     BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("practiceBindModel",practiceBindModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.practiceBindModel",result);
            return "redirect:add";
        }

        try{
            PracticeViewModel practice =  this.practiceService.addPractice(practiceBindModel);
        }catch (Error e){

        }
        redirectAttributes.addFlashAttribute("userType","ADMIN");
        redirectAttributes.addFlashAttribute("pName","empty");
        return "redirect:/admin/admin-home";
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN')")
    public String detail(Model model,@RequestParam(value = "pName",required = false)String pName,
                         HttpSession session,@AuthenticationPrincipal Principal principal){

       try {
           PracticeDetailsModel practice = this.practiceService.getPracticeByName(pName);
           model.addAttribute("practice",practice);
       }catch (Exception e){
             return ("redirect:/admin/admin-home");
       }
        session.setAttribute("pName",pName);
        model.addAttribute("user",principal);
        return "practice-details";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(Model model,@RequestParam(value = "action",required = false)String action,
                       HttpSession session,@AuthenticationPrincipal Principal principal,
                       RedirectAttributes redirectAttributes){
       if(action!=null) {
           switch (action) {
               case "deactivate":
                   this.practiceService.deactivate((String) session.getAttribute("pName"));
                   break;
               case "activate":
                   this.practiceService.activate((String) session.getAttribute("pName"));
                   break;
               case "edit":
                   return "redirect:editpractice";
               default:
                   break;
           }
       }
        String redirect = String.format("redirect:details?pName=%s", URLEncoder.encode((String)session.getAttribute("pName"), StandardCharsets.UTF_8));
        return redirect;
    }

    @GetMapping("/editpractice")
    @PreAuthorize("hasRole('ADMIN')")
    public String editPractice(Model model,HttpSession session,@AuthenticationPrincipal Principal principal){
        String pName = (String) session.getAttribute("pName");
        model.addAttribute("message","Редактиране на практика !");
        model.addAttribute("user",principal);

        if(!model.containsAttribute("practiceBindModel")){
            PracticeEditModel pModel = this.practiceService.findPracticeByName(pName);
            session.setAttribute("help",pModel.getId());
            PracticeBindModel bModel = this.mapper.map(pModel,PracticeBindModel.class);
            model.addAttribute("practiceBindModel",bModel);
        }
        return "practice-edit";
     }

    @PostMapping("/editpractice")
    @PreAuthorize("hasRole('ADMIN')")
    public String deitConfirm(@Valid @ModelAttribute(value = "practiceBindModel")PracticeBindModel practiceBindModel,
                              BindingResult result,RedirectAttributes redirectAttributes,HttpSession session){

         if(result.hasErrors()){
             redirectAttributes.addFlashAttribute("practiceBindModel",practiceBindModel);
             redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.practiceBindModel",result);
             return "redirect:editpractice";
         }

         try{
            PracticeEditModel pModel = this.practiceService.getPracticeById((String)session.getAttribute("help"));
            pModel.setName(practiceBindModel.getName());
            pModel.setPhoneNumber(practiceBindModel.getPhoneNumber());
            pModel.setRegNumber(practiceBindModel.getRegNumber());
            pModel.setLogo(practiceBindModel.getLogo());
            this.practiceService.editPractice(pModel);
         }catch (Error e){
             redirectAttributes.addFlashAttribute("message","Неуспешно редактиране на практиката !");
             return "redirect:details";
         }
        redirectAttributes.addFlashAttribute("message","Успешно редактиране на практиката !");
        String redirect = String.format("redirect:details?pName=%s", URLEncoder.encode((String)session.getAttribute("pName"), StandardCharsets.UTF_8));
         return redirect;
     }



}
