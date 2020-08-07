package presentation.demo.restcontrollers;

import com.google.gson.Gson;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import presentation.demo.models.bindmodels.MessageBindModel;
import presentation.demo.models.bindmodels.MessageSendModel;
import presentation.demo.models.viewmodels.MessageViewModel;
import presentation.demo.services.MessageService;

import javax.naming.NoPermissionException;
import java.util.List;

@RestController
@RequestMapping("/info")
@PreAuthorize("hasAnyRole('ADMIN','NURSE','PATIENT','DOCTOR')")
public class MessageRestController {
    private final MessageService messageService;
    private final Gson json;

    public MessageRestController(MessageService messageService, Gson json) {
        this.messageService = messageService;
        this.json = json;
    }

    @GetMapping("/count/{number}")
    public String countMessages(@PathVariable(value = "number") String number) {
        long count = this.messageService.countMessagesByUser(number);
        return Long.toString(count);
//        return "5";
    }

    @PutMapping("/new")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> leftNewMessage(@RequestBody String body) throws NoPermissionException, NotFoundException {
        MessageBindModel model = json.fromJson(body, MessageBindModel.class);
        this.messageService.leaveMessage(model);

        return ResponseEntity.ok("Съобщението беше доставено!");
    }

    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @PutMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody String body) throws NotFoundException, NoPermissionException {
        MessageSendModel model = json.fromJson(body,MessageSendModel.class);
        this.messageService.sendMessage(model);
        return ResponseEntity.ok("Съобщението беше доставено!");
    }

    @GetMapping("/unreaded/{username}")
    public List<MessageViewModel> getUnreadMessages(@PathVariable(value = "username") String username) {
        List<MessageViewModel> list = this.messageService.getUnreadMessagesByUser(username);
        return list;
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<?> getSingleMessage(@PathVariable(value = "id") String id) throws NotFoundException {
        MessageViewModel message;
        message = this.messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("admin")
    public ResponseEntity<?> deleteAllAdminMessages() throws NotFoundException {
        this.messageService.clearAdminMessages();
        return ResponseEntity.ok("message cleared");
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
}
