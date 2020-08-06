package presentation.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import presentation.demo.models.entities.Message;
import presentation.demo.models.entities.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {


    @Query(value = "select count(m) from Message as m where m.read = false and m.recipient.username = ?1")
    long countUnreadMessagesByUser(String num);

    @Query(value = "select m from Message as m where m.read=false and m.recipient.username=?1 order by m.leftAt desc ")
    List<Message> getUnreadMessagesByUser(String username);

    @Modifying
    @Transactional
    @Query(value = "delete  from Message m where m.read=true and m.leftAt < ?1")
    void clearMessages(LocalDateTime time);

    @Modifying
    @Transactional
    @Query(value = "delete from Message as m where m.leftAt < ?1")
    void clearOldMessages(LocalDateTime time);

    @Modifying
    @Transactional
    @Query(value = "delete from Message as m where m.author.username = :name")
    void clearAdminMessages(@Param("name")String name);

    @Modifying
    @Transactional
    void deleteByAuthor(User author);
}
