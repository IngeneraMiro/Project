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
import presentation.demo.services.serviceImpl.AuthorityServiceImpl;

@SpringBootTest
public class AuthorityServiceTest {
    private Authority testAuthority;

    @Mock
    AuthorityRepository authorityRepository;
    @Mock
    ModelMapper mapper;
    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @BeforeEach
    void setup(){
        testAuthority = new Authority();
        testAuthority.setId("firstAuthority");
        testAuthority.setAuthority("ROLE_ADMIN");

        this.authorityService = new AuthorityServiceImpl(authorityRepository,mapper);
    }

    @Test
    public void testGetAuthority(){
//    Arrange
        Authority expected = testAuthority;
        Mockito.when(this.authorityRepository.findByAuthority("ROLE_ADMIN")).thenReturn(testAuthority);
//    Act
        Authority result = this.authorityService.getAuthorityByAuthority("ROLE_ADMIN");
//    Assert
        Assert.assertEquals(expected,result);
    }



}
