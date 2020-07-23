package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {

   Authority findByAuthority(String authority);

}
