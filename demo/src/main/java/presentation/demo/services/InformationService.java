package presentation.demo.services;

import javassist.NotFoundException;
import presentation.demo.models.bindmodels.InformationBindModel;
import presentation.demo.models.entities.Information;
import presentation.demo.models.viewmodels.InformationViewModel;

import javax.naming.NoPermissionException;

public interface InformationService {

    Information saveInfo(InformationBindModel model) throws NoPermissionException, NotFoundException;

    InformationViewModel getInfoByType(String type) throws NotFoundException;

    boolean deleteInformation(String type);
}
