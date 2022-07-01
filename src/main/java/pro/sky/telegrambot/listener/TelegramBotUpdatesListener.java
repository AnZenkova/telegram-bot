package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.data.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                notificationTask.setText(item);
                notificationTask.setIdChar(update.message().chat().id());
                notificationTask.setDateTime(dateTime);
                notificationTask.setKey(1l);
                notificationTaskRepository.save(notificationTask);
                //telegramBot.execute(new SendMessage(update.message().chat().id(), notificationTask.getText()));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
