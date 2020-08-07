package presentation.demo.services.serviceImpl;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import presentation.demo.models.bindmodels.InformationBindModel;
import presentation.demo.models.entities.Information;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.InformationViewModel;
import presentation.demo.repositories.InfoRepository;
import presentation.demo.services.InformationService;
import presentation.demo.services.UserService;

import javax.naming.NoPermissionException;
import java.time.LocalDateTime;

@Service
public class InformationServiceImpl implements InformationService {
    private final ModelMapper mapper;
    private final UserService userService;
    private final InfoRepository infoRepository;


    public InformationServiceImpl(ModelMapper mapper, UserService userService, InfoRepository infoRepository) {
        this.mapper = mapper;
        this.userService = userService;
        this.infoRepository = infoRepository;
    }


    @Override
    public Information saveInfo(InformationBindModel model) throws NoPermissionException, NotFoundException {
        if(model.getAuthor().charAt(0)!='A'){
            throw new NoPermissionException("Нямате Права да записвате информация!");
        }
       Information info = this.infoRepository.findByType(model.getType()).orElse(null);
       if(info==null){
           info = this.mapper.map(model,Information.class);
           info.setLeftOn(LocalDateTime.now());
           User author = this.userService.getUserByRegNumber(model.getAuthor());
           info.setAuthor(author);
           return this.infoRepository.saveAndFlush(info);
       }
       info.setBody(model.getBody());
       info.setLeftOn(LocalDateTime.now());
        return this.infoRepository.saveAndFlush(info);
    }

    @Override
    public InformationViewModel getInfoByType(String type) throws NotFoundException {
       Information info =  this.infoRepository.findByType(type).orElseThrow(()->new NotFoundException("Не намерихме търсената от Вас информация!"));

       return this.mapper.map(info,InformationViewModel.class);
    }

    @Override
    public boolean deleteInformation(String type) {
        this.infoRepository.deleteByType(type);
        return true;
    }
}
