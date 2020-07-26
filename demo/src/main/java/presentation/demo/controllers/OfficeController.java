package presentation.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("offices")
@PreAuthorize("hasAnyRole('ADMIN','MAIN')")
public class OfficeController {

    @GetMapping("/add")
    public String addOffice(){

    }

    @PostMapping("/add")
    public String addOfficeConfirm(){

    }

}
