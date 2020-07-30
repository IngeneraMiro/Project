package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Office;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office,String> {

 @Query(value = "select o from Office as o where o.practice.name = ?1")
 List<Office> getOfficesByPracticeName(String pName);

}
