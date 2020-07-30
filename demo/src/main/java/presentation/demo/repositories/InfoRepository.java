package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Information;

import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Information,String> {

   Optional<Information> findByType(String type);

}
