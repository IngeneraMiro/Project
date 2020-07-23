package presentation.demo.services;

import javassist.NotFoundException;
import presentation.demo.models.bindmodels.MessageBindModel;
import presentation.demo.models.entities.Message;
import presentation.demo.models.viewmodels.MessageViewModel;

import javax.naming.NoPermissionException;
import java.util.List;

public interface MessageService {

    long countMessagesByUser(String number);
    Message leaveMessage(MessageBindModel model) throws NoPermissionException, NotFoundException;
    List<MessageViewModel> getUnreadMessagesByUser(String username);
}
