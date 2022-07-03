package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.data.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String msg = update.message().text();
            String date = null;
            String item = null;
            NotificationTask notificationTask = new NotificationTask();
            if (msg.equals("/start")) {
                telegramBot.execute(new SendMessage(update.message().chat().id(), "Здравствуйте)"));
            } else if (msg != null && msg != "/start") {
                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher1 = pattern.matcher(msg);
                if (matcher1.matches()) {

                    date = matcher1.group(1);
                    item = matcher1.group(3);
                }
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    notificationTask.setText(item);
                    notificationTask.setIdChar(update.message().chat().id());
                    notificationTask.setDateTime(dateTime);
                    notificationTaskRepository.save(notificationTask);
                } catch (NullPointerException e) {
                    telegramBot.execute(new SendMessage(update.message().chat().id(), "Не верный формат даты"));
                }
            }
            Collection<NotificationTask> coincidence = run();
            if (coincidence != null) {
                for (int i = 0; i < coincidence.size(); i++) {
                    List<String> printText = (List<String>) printText(coincidence);
                    telegramBot.execute(new SendMessage(update.message().chat().id(), printText.get(i)));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public Collection<NotificationTask> run() {
        return notificationTaskRepository.getNotificationTaskByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    private Collection<String> printText(Collection<NotificationTask> coincidence) {

        return coincidence.stream()
                .map(s -> s.getText())
                .collect(Collectors.toList());
    }

}
