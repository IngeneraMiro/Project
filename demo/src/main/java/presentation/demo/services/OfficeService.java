package presentation.demo.services;

import javassist.NotFoundException;
import presentation.demo.models.bindmodels.OfficeBindModel;
import presentation.demo.models.entities.Office;
import presentation.demo.models.viewmodels.OfficeViewModel;

import java.util.List;

public interface OfficeService {

    Office addNewOffice(OfficeBindModel model);

    List<OfficeViewModel> getOfficesByPractice(String pName);

    void deleteOfficeById(String id) throws NotFoundException;
}
