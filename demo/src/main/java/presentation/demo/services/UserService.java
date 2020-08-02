package presentation.demo.services;

import javassist.NotFoundException;
import presentation.demo.models.bindmodels.UserBindModel;
import presentation.demo.models.entities.User;
import presentation.demo.models.viewmodels.UserControlViewModel;
import presentation.demo.models.viewmodels.UserViewModel;

import java.util.List;

public interface UserService {

    User addUser(UserBindModel model);
    Boolean begin();
    User getUserByRegNumber(String number) throws NotFoundException;
    List<UserViewModel>  getActiveDoctorsByPractice(String practice);
    User addMainDoctor(String username) throws NotFoundException;
    User doNormalDoctor(String username) throws NotFoundException;
    UserControlViewModel getUserControlModel(String username) throws NotFoundException;
    User getByNamesAndPractice(String firstName,String lastName,String pName) throws NotFoundException;

}
