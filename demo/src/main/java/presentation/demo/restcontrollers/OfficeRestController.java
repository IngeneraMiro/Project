package presentation.demo.restcontrollers;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.viewmodels.OfficeViewModel;
import presentation.demo.services.OfficeService;

import java.util.List;

@RestController
@RequestMapping("/office")
@PreAuthorize("hasAnyRole('ADMIN','MAIN')")
public class OfficeRestController {
    private final OfficeService officeService;

    public OfficeRestController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/get/{pName}")
    public List<OfficeViewModel> getOfficesByPractice(@PathVariable(value = "pName")String pName){
       List<OfficeViewModel> list = this.officeService.getOfficesByPractice(pName);

       return list;
    }


    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable(value = "id")String id) throws NotFoundException {
        this.officeService.deleteOfficeById(id);
        String message = "Успешно изтрит !";
        return ResponseEntity.ok(message);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> catchNotFoundEx(NotFoundException ex) {
        ResponseEntity response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        return response;
    }
}
