package presentation.demo.restcontrollers;

import com.google.gson.Gson;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.bindmodels.InformationBindModel;
import presentation.demo.models.entities.Information;
import presentation.demo.models.viewmodels.InformationViewModel;
import presentation.demo.services.InformationService;
import presentation.demo.utils.fileutil.FileUtil;

import javax.naming.NoPermissionException;

import java.io.IOException;

import static presentation.demo.global.GlobalConstants.FILE_ADDRESS;

@RestController
@RequestMapping("/information")
@PreAuthorize("hasRole('ADMIN')")
public class InformationRestController {
    private final Gson json;
    private final FileUtil fUtil;
    private final InformationService informationService;

    @Autowired
    public InformationRestController(Gson json, FileUtil fUtil, InformationService informationService) {
        this.json = json;
        this.fUtil = fUtil;
        this.informationService = informationService;
    }

    @PutMapping("/new")
    public ResponseEntity<?> leftNewInfo(@RequestBody String body) throws NoPermissionException, NotFoundException {
        InformationBindModel model = json.fromJson(body,InformationBindModel.class);
        Information info = this.informationService.saveInfo(model);
        return ResponseEntity.ok("Информацията е записана!");
    }

    @GetMapping("/getinfo/{type}")
    @PreAuthorize("permitAll()")
    public String getInfoByType(@PathVariable(value = "type")String type) throws NotFoundException {
        InformationViewModel model = this.informationService.getInfoByType(type);

        return model.getBody();
    }

    @GetMapping("/log")
    public String getLog() throws IOException {
    String log = fUtil.readFileContent(FILE_ADDRESS);
      return log;
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> catchNotFoundEx(NotFoundException ex) {
        ResponseEntity response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        return response;
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<?> catchNotPermissionEx(NoPermissionException ex) {
        ResponseEntity response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        return response;
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> catchIOExeption(IOException ex) {
        ResponseEntity response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        return response;
    }

}
