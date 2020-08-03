package presentation.demo.unit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import presentation.demo.models.bindmodels.PracticeBindModel;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.viewmodels.PracticeDetailsModel;
import presentation.demo.models.viewmodels.PracticeEditModel;
import presentation.demo.models.viewmodels.PracticeViewModel;
import presentation.demo.repositories.PracticeRepository;
import presentation.demo.services.serviceImpl.PracticeServiceImpl;

import javax.swing.*;
import java.time.LocalDateTime;

@SpringBootTest
public class PracticeServiceTest {

    private Practice fPractice;
    private Practice sPractice;
    private PracticeEditModel editModel;
    private PracticeDetailsModel detailsModel;
    private PracticeViewModel viewModel;
    private PracticeBindModel bindModel;

    @Autowired
    ModelMapper mapper;
    @Mock
    PracticeRepository practiceRepository;
    @InjectMocks
    PracticeServiceImpl practiceService;

    @BeforeEach
    void setup(){

        testLoad();
        practiceService = new PracticeServiceImpl(this.practiceRepository,this.mapper);
    }

    @Test
    public void testFindPracticeByName(){

    }

    @Test
    public void testGetPracticeById(){
//    Arrange
        PracticeEditModel expected = editModel;
        Mockito.when(this.practiceRepository.getOne(sPractice.getId())).thenReturn(sPractice);
//    Act
        PracticeEditModel result = this.practiceService.getPracticeById(sPractice.getId());
//    Assert
        Assert.assertEquals(expected.getName(),result.getName());
        Assert.assertEquals(expected.getRegNumber(),result.getRegNumber());
        Assert.assertEquals(expected.getId(),result.getId());
    }

    @Test
    public void testEditPractice(){
//    Arrange
        Practice expected = sPractice;
        Mockito.when(this.practiceRepository.findById(sPractice.getId())).thenReturn(java.util.Optional.ofNullable(sPractice));
        Mockito.when(this.practiceRepository.saveAndFlush(Mockito.any(Practice.class))).thenReturn(sPractice);
//    Act
        Practice result = this.practiceService.editPractice(editModel);
//    Assert
        Assert.assertEquals(expected,result);
    }


    void testLoad(){
        fPractice = new Practice();
        fPractice.setActive(true);
        fPractice.setName("Витал ООД");
        fPractice.setCreatedOn(LocalDateTime.now());
        fPractice.setLogo("Витал-ООД");
        fPractice.setPhoneNumber("0897984545");
        fPractice.setRegNumber("GP8547fr74");
        fPractice.setId("firstPractice");

        sPractice = new Practice();
        sPractice.setActive(true);
        sPractice.setName("Здравец ЕТ");
        sPractice.setCreatedOn(LocalDateTime.now());
        sPractice.setLogo("Здравец-ЕТ");
        sPractice.setPhoneNumber("0888234567");
        sPractice.setRegNumber("GPhf74fke3");
        sPractice.setId("secondPractice");

        this.editModel = new PracticeEditModel();
        editModel.setId(sPractice.getId());
        editModel.setLogo(sPractice.getLogo());
        editModel.setName(sPractice.getName());
        editModel.setPhoneNumber(sPractice.getPhoneNumber());
        editModel.setRegNumber(sPractice.getRegNumber());

        this.detailsModel = new PracticeDetailsModel();
        detailsModel.setActive(sPractice.isActive());
        detailsModel.setCreatedOn(sPractice.getCreatedOn());
        detailsModel.setLogo(sPractice.getLogo());
        detailsModel.setName(sPractice.getName());
        detailsModel.setPhoneNumber(sPractice.getPhoneNumber());
        detailsModel.setRegNumber(sPractice.getRegNumber());

        this.viewModel = new PracticeViewModel();
        viewModel.setId(sPractice.getId());
        viewModel.setName(sPractice.getName());
        viewModel.setRegNumber(sPractice.getRegNumber());

        this.bindModel = new PracticeBindModel();
        bindModel.setLogo(sPractice.getLogo());
        bindModel.setName(sPractice.getName());
        bindModel.setPhoneNumber(sPractice.getPhoneNumber());
        bindModel.setRegNumber(sPractice.getRegNumber());
    }
}
