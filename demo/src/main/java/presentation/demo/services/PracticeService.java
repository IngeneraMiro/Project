package presentation.demo.services;

import presentation.demo.models.bindmodels.PracticeBindModel;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.viewmodels.PracticeDetailsModel;
import presentation.demo.models.viewmodels.PracticeEditModel;
import presentation.demo.models.viewmodels.PracticeViewModel;

import java.util.List;

public interface PracticeService {

    List<String> getAllActivePractice();
    List<String> getAllPractices();
    void deactivate(String name);
    void  activate(String name);
    Practice getByName(String name);
    PracticeViewModel addPractice(PracticeBindModel model);
    PracticeDetailsModel getPracticeByName(String name);
    PracticeEditModel findPracticeByName(String name);
    PracticeEditModel getPracticeById(String id);
    Practice editPractice(PracticeEditModel model);
}
