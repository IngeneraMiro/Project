package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

  @Query(value = "select u from User as u where u.username = ?1")
  Optional<User> findUserWithUsername(String username);

  boolean existsByUsername(String username);

  @Query(value = "select u from User as u where u.enabled=true and u.isDoctor = true and u.practice.name = ?1")
  List<User> getDoctorsByPractice(String practice);

  @Query(value = "select u from User as u where u.enabled=true and u.isDoctor = true and u.enabled = true and u.lastName = :lName and u.practice.name = :pName")
  User getDoctorFromPractice(@Param("lName")String lastName,@Param("pName") String practice);

  @Query(value = "select u from User as u where u.enabled = true and u.firstName = ?1 and u.lastName = ?2 and u.practice.name = ?3")
  Optional<User> findByNamesAndPractice(String fName,String lName,String pName);

  @Query(value = "select u from User as u where u.doctor.username = ?1 and u.isNurse = true and u.enabled = true ")
  User getNurseByDoc(String docUsername);

  User getByFirstName(String fName);

  @Query(value = "delete from User as u where u.firstName = ?1")
  void  removeTestByFirstName(String fName);
}
