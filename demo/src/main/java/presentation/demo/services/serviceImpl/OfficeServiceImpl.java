package presentation.demo.services.serviceImpl;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import presentation.demo.models.bindmodels.OfficeBindModel;
import presentation.demo.models.entities.Office;
import presentation.demo.models.viewmodels.OfficeViewModel;
import presentation.demo.repositories.OfficeRepository;
import presentation.demo.services.OfficeService;
import presentation.demo.services.PracticeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepository officeRepository;
    private final PracticeService practiceService;
    private final ModelMapper mapper;


    public OfficeServiceImpl(OfficeRepository officeRepository, PracticeService practiceService, ModelMapper mapper) {
        this.officeRepository = officeRepository;
        this.practiceService = practiceService;
        this.mapper = mapper;
    }

    @Override
    public Office addNewOffice(OfficeBindModel model) {
      Office office = this.mapper.map(model,Office.class);
      office.setPractice(this.practiceService.getByName(model.getPractice()));
        return this.officeRepository.saveAndFlush(office);
    }

    @Override
    public List<OfficeViewModel> getOfficesByPractice(String pName) {
        List<OfficeViewModel> list = this.officeRepository.getOfficesByPracticeName(pName).stream().map(o->this.mapper.map(o,OfficeViewModel.class)).collect(Collectors.toList());
        return list;
    }

    @Override
    public boolean deleteOfficeById(String id) throws NotFoundException {
        if(this.officeRepository.existsById(id)) {
            this.officeRepository.deleteById(id);
            return true;
        }else {
            throw new NotFoundException("На този адрес няма офис!");
        }
    }
}
