package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {


    @Query(value = "select count(m) from Message as m where m.read = false and m.recipient.username = ?1")
    long countUnreadMessagesByUser(String num);

    @Query(value = "select m from Message as m where m.read=false and m.recipient.username=?1 order by m.leftAt desc ")
    List<Message> getUnreadMessagesByUser(String username);
}
