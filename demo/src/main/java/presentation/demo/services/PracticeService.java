package presentation.demo.services;

import org.springframework.security.access.prepost.PreAuthorize;
import presentation.demo.models.bindmodels.PracticeBindModel;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.viewmodels.PracticeDetailsModel;
import presentation.demo.models.viewmodels.PracticeEditModel;
import presentation.demo.models.viewmodels.PracticeViewModel;

import java.util.List;

public interface PracticeService {

    List<String> getAllActivePractice();
    List<String> getAllPractices();
    @PreAuthorize("hasRole('ADMIN')")
    void deactivate(String name);
    @PreAuthorize("hasRole('ADMIN')")
    void  activate(String name);
    Practice getByName(String name);
    @PreAuthorize("hasRole('ADMIN')")
    PracticeViewModel addPractice(PracticeBindModel model);
    PracticeDetailsModel getPracticeByName(String name);
    PracticeEditModel findPracticeByName(String name);
    PracticeEditModel getPracticeById(String id);
    Practice editPractice(PracticeEditModel model);
}
