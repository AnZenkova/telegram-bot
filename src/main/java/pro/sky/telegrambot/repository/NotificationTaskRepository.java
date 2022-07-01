package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.data.NotificationTask;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

}
