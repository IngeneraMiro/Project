package presentation.demo.unit;

import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import presentation.demo.models.bindmodels.OfficeBindModel;
import presentation.demo.models.entities.Office;
import presentation.demo.models.entities.Practice;
import presentation.demo.models.viewmodels.OfficeViewModel;
import presentation.demo.repositories.OfficeRepository;
import presentation.demo.services.PracticeService;
import presentation.demo.services.serviceImpl.OfficeServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OfficeServiceTest {

    private Office testOffice;
    private Practice testPractice;

    @Autowired
    ModelMapper mapper;

    @Mock
    OfficeRepository officeRepository;
    @Mock
    PracticeService practiceService;
    @InjectMocks
    OfficeServiceImpl officeService;

    @BeforeEach
    void setup() {
        this.officeRepository = Mockito.mock(OfficeRepository.class);
        testLoad();
        this.officeService = new OfficeServiceImpl(this.officeRepository, this.practiceService, this.mapper);
    }

    @Test
    public void testGetOfficesByPractice() {
//    Arrange
        List<Office> list = new ArrayList<>();
        list.add(testOffice);
        List<OfficeViewModel> expected = new ArrayList<>();
        expected.add(this.mapper.map(testOffice, OfficeViewModel.class));
        Mockito.when(this.officeRepository.getOfficesByPracticeName("Test practice")).thenReturn(list);
//    Act
        List<OfficeViewModel> result = this.officeService.getOfficesByPractice("Test practice");
//    assert
        Assert.assertEquals(expected.size(), result.size());
        Assert.assertEquals(expected.get(0).getAddress(), result.get(0).getAddress());

    }

    @Test
    public void testAddNewOffice() {
//    Arrange
        Office expected = testOffice;
        OfficeBindModel model = new OfficeBindModel();
        model.setAddress("My address");
        model.setPhone("08888556677");
        model.setSchedule("09:00 - 17:00");
        model.setPractice("Test practice");
        Mockito.when(this.practiceService.getByName("Test practice")).thenReturn(testPractice);
        Mockito.when(this.officeRepository.saveAndFlush(Mockito.any(Office.class))).thenReturn(testOffice);
//    Act
        Office result = this.officeService.addNewOffice(model);
//    Assert
        Assert.assertEquals(expected.getAddress(),result.getAddress());
        Assert.assertEquals(expected.getPractice(),result.getPractice());
    }


    @Test
    public void testDeleteOfficeByIdException(){
//    Arrange
        String expected = "На този адрес няма офис!";
        Mockito.when(this.officeRepository.existsById("secondOffice")).thenReturn(false);
//    Act/Assert
        try {
            this.officeService.deleteOfficeById("secondOffice");
        } catch (NotFoundException e) {
            Assert.assertEquals(expected,e.getMessage());
        }
    }


    void testLoad() {
        this.testPractice = new Practice();
        testPractice.setId("firstPractice");
        testPractice.setRegNumber("№547283");
        testPractice.setPhoneNumber("08888556677");
        testPractice.setCreatedOn(LocalDateTime.now());
        testPractice.setName("Test practice");
        testPractice.setActive(true);

        this.testOffice = new Office();
        testOffice.setAddress("My address");
        testOffice.setPhone("08888556677");
        testOffice.setPractice(testPractice);
        testOffice.setId("firstOffice");
        testOffice.setSchedule("09:00 - 17:00");

    }
}
