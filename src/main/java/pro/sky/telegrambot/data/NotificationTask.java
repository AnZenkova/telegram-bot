package pro.sky.telegrambot.data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task", schema = "public")
public class NotificationTask {


    @Id
    @GeneratedValue
    private Long key;
    private Long idChar;
    private String text;
    private LocalDateTime dateTime;

    public NotificationTask(Long key, Long idChar, String text, LocalDateTime dateTime) {
        this.key = key;
        this.idChar = idChar;
        this.text = text;
        this.dateTime = dateTime;
    }

    public NotificationTask() {

    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getIdChar() {
        return idChar;
    }

    public void setIdChar(Long idChar) {
        this.idChar = idChar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(key, that.key) && Objects.equals(idChar, that.idChar) && Objects.equals(text, that.text) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, idChar, text, dateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "key=" + key +
                ", idChar=" + idChar +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
