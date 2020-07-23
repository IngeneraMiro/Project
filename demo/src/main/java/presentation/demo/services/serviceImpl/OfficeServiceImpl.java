package presentation.demo.services.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import presentation.demo.repositories.OfficeRepository;
import presentation.demo.services.OfficeService;

@Service
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepository officeRepository;
    private final ModelMapper mapper;


    public OfficeServiceImpl(OfficeRepository officeRepository, ModelMapper mapper) {
        this.officeRepository = officeRepository;
        this.mapper = mapper;
    }
}
