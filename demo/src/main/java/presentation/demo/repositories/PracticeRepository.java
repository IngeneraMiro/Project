package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Practice;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeRepository extends JpaRepository<Practice,String> {

    @Query("select p.name from Practice as p where p.active = true ")
    List<String> getAllActivePractice();

    @Query("select p.name from Practice as p ")
    List<String> getAllPractice();

    @Transactional
    @Modifying
    @Query("update Practice set active = false where name = ?1")
    void deactivate(String name);

    @Transactional
    @Modifying
    @Query("update Practice set active = true where name = ?1")
    void activate(String name);

    Practice findByName(String name);

    Optional<Practice> findById(String id);
}
