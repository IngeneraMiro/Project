package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Information;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Information,String> {

   Optional<Information> findByType(String type);

   @Modifying
   @Transactional
   void deleteByType(String type);
}
