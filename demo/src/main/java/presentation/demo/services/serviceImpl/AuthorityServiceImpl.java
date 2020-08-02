package presentation.demo.services.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import presentation.demo.models.entities.Authority;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.services.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final ModelMapper mapper;


    public AuthorityServiceImpl(AuthorityRepository authorityRepository, ModelMapper mapper) {
        this.authorityRepository = authorityRepository;
        this.mapper = mapper;
    }

    @Override
    public Authority getAuthorityById(String authority) {
        return this.authorityRepository.findByAuthority(authority);
    }
}
