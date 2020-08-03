package presentation.demo.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import presentation.demo.models.entities.Authority;
import presentation.demo.repositories.AuthorityRepository;
import presentation.demo.services.serviceImpl.AuthorityServiceImpl;

@SpringBootTest
public class AuthorityServiceTest {
    private static final String AUTHORITY_NAME = "ROLE_ADMIN";

    @Autowired
    ModelMapper mapper;
    @InjectMocks
    AuthorityServiceImpl authorityService;
    @Mock
    AuthorityRepository authorityRepository;

    private Authority testAuthority;

    @BeforeEach
    void setup(){

        authorityRepository = Mockito.mock(AuthorityRepository.class);
        testAuthority = new Authority();
        testAuthority.setId("firstAuthority");
        testAuthority.setAuthority(AUTHORITY_NAME);
        authorityService = new AuthorityServiceImpl(authorityRepository,mapper);


    }

    @Test
    public void testGetAuthorityByName(){
//    Arrange
        Mockito.when(this.authorityRepository.findByAuthority(AUTHORITY_NAME)).thenReturn(testAuthority);
        Authority expected = testAuthority;
//    Act
        Authority result = this.authorityService.getAuthorityById(AUTHORITY_NAME);
//    Assert
        Assert.assertEquals(expected,result);
    }


}
