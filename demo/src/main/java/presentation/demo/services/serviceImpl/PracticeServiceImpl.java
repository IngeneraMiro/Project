package presentation.demo.services.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import presentation.demo.models.bindmodels.PracticeBindModel;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.viewmodels.PracticeDetailsModel;
import presentation.demo.models.viewmodels.PracticeEditModel;
import presentation.demo.models.viewmodels.PracticeViewModel;
import presentation.demo.repositories.PracticeRepository;
import presentation.demo.services.PracticeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PracticeServiceImpl implements PracticeService {
    private final PracticeRepository practiceRepository;
    private final ModelMapper mapper;

    public PracticeServiceImpl(PracticeRepository practiceRepository, ModelMapper mapper) {
        this.practiceRepository = practiceRepository;
        this.mapper = mapper;
    }

    @Override
    public List<String> getAllActivePractice() {
        return this.practiceRepository.getAllActivePractice();
    }

    @Override
    public List<String> getAllPractices() {
        return this.practiceRepository.getAllPractice();
    }

    @Override
    public void deactivate(String name) {
        this.practiceRepository.deactivate(name);
    }

    @Override
    public void activate(String name) {
       this.practiceRepository.activate(name);
    }

    @Override
    public Practice getByName(String name) {
        return this.practiceRepository.findByName(name);
    }

    @Override
    public PracticeViewModel addPractice(PracticeBindModel model) {
        Practice practice = this.mapper.map(model, Practice.class);
        practice.setCreatedOn(LocalDateTime.now());
        practice.setActive(true);
        Practice result = this.practiceRepository.saveAndFlush(practice);
        return this.mapper.map(result,PracticeViewModel.class);
    }

    @Override
    public PracticeDetailsModel getPracticeByName(String name) {
        return this.mapper.map(this.practiceRepository.findByName(name),PracticeDetailsModel.class);
    }

    @Override
    public PracticeEditModel findPracticeByName(String name) {
        return this.mapper.map(this.practiceRepository.findByName(name),PracticeEditModel.class);
    }

    @Override
    public PracticeEditModel getPracticeById(String id) {
        return this.mapper.map(this.practiceRepository.getOne(id),PracticeEditModel.class);
    }

    @Override
    public Practice editPractice(PracticeEditModel model) {
       Practice practice = this.practiceRepository.findById(model.getId()).orElse(null);
       practice.setName(model.getName());
       practice.setRegNumber(model.getRegNumber());
       practice.setPhoneNumber(model.getPhoneNumber());
       practice.setLogo(model.getLogo());
       return this.practiceRepository.saveAndFlush(practice);
    }
}
