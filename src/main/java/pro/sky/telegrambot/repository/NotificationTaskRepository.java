package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.data.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;


public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Integer> {

    @Query("FROM NotificationTask WHERE dateTime < CURRENT_TIMESTAMP")
    public Collection<NotificationTask> findNotificationTaskByDateTime(LocalDateTime now);
}
