package presentation.demo.unit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import presentation.demo.models.entities.Authority;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.services.AuthorityService;
import presentation.demo.services.serviceImpl.AuthorityServiceImpl;

@SpringBootTest
public class AuthorityServiceTest {

    private Authority testAuthority;

    @InjectMocks
    private AuthorityServiceImpl authorityService;
    @Mock
    AuthorityRepository authorityRepository;
    @Mock
    ModelMapper mapper;

    @BeforeEach
    void setup(){
        this.authorityRepository = Mockito.mock(AuthorityRepository.class);
        this.authorityService = new AuthorityServiceImpl(authorityRepository,mapper);
        testAuthority = new Authority();
        testAuthority.setId("firstAuthority");
        testAuthority.setAuthority("ROLE_ADMIN");
    }

    @Test
    public void testGetAuthority(){
//    Arrange
        Mockito.when(this.authorityRepository.findByAuthority("ROLE_ADMIN")).thenReturn(testAuthority);
//    Act
        Authority result = this.authorityService.getAuthorityByAuthority("ROLE_ADMIN");
//    Assert
        Assert.assertEquals(testAuthority,result);
    }



}
