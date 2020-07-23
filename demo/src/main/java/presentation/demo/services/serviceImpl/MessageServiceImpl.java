package presentation.demo.services.serviceImpl;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import presentation.demo.models.bindmodels.MessageBindModel;
import presentation.demo.models.entities.Message;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.MessageViewModel;
import presentation.demo.repositories.MessageRepository;
import presentation.demo.services.MessageService;
import presentation.demo.services.UserService;

import javax.naming.NoPermissionException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ModelMapper mapper;


    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, ModelMapper mapper) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public long countMessagesByUser(String number) {
        return this.messageRepository.countUnreadMessagesByUser(number);
    }

    @Override
    public Message leaveMessage(MessageBindModel model) throws NoPermissionException, NotFoundException {
        Message message = new Message();
        User recipient = new User();
        User user = userService.getUserByRegNumber(model.getSendfrom());
        try {
            recipient = this.userService.getByNamesAndPractice(model.getFname(), model.getTname(), user.getPractice().getName());
            message.setRecipient(recipient);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }

        if ((user.getUsername().charAt(0) == 'P' && recipient.getUsername().charAt(0) == 'D') || (user.getUsername().charAt(0) == 'P' && recipient.getUsername().charAt(0) == 'A')) {
            throw new NoPermissionException("Можете да оставите съобщение на медицинската сестра!");
        }
        message.setAuthor(user);
        message.setBody(model.getMess());
        message.setRead(false);
        message.setLeftAt(LocalDateTime.now());

        return this.messageRepository.saveAndFlush(message);
    }

    @Override
    @Transactional
    public List<MessageViewModel> getUnreadMessagesByUser(String username) {
        List<Message> list = this.messageRepository.getUnreadMessagesByUser(username);
        List<MessageViewModel> toReturn = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Message m : list) {
            MessageViewModel model = new MessageViewModel();
            model.setId(m.getId());
            model.setBody(m.getBody());
            model.setLeftFrom(m.getAuthor().getFirstName() + " " + m.getAuthor().getLastName());
            model.setLeftOn(m.getLeftAt().format(formatter));
            toReturn.add(model);
        }

        return toReturn;
    }

    @Override
    public MessageViewModel getMessageById(String id) throws NotFoundException {
        Message message = this.messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Не намерихме това съобщение!"));
        MessageViewModel model = new MessageViewModel();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.setId(message.getId());
        model.setBody(message.getBody());
        model.setLeftFrom(message.getAuthor().getFirstName() + " " + message.getAuthor().getLastName());
        model.setLeftOn(message.getLeftAt().format(formatter));
        message.setRead(true);
        this.messageRepository.saveAndFlush(message);
        return model;
    }
}
