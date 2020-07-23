package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office,String> {



}
