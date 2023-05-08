package pl.moderr.moderrkowo.core.notifications;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.notifications.exceptions.MNotification_CannotPublish;
import pl.moderr.moderrkowo.core.notifications.exceptions.MNotification_NotPublished;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class MNotification {

    private final UUID notificationId;
    private final UUID playerId;
    private final NotifycationType type;
    private boolean published;
    private String data;

    @Contract(pure = true)
    public MNotification(UUID notificationId, boolean published, UUID playerId, NotifycationType type, String data) {
        this.notificationId = notificationId;
        this.published = published;
        this.playerId = playerId;
        this.type = type;
        this.data = data;
    }

    public void Publish() throws MNotification_CannotPublish, SQLException {
        if (published) {
            throw new MNotification_CannotPublish();
        } else {
            String sql = "INSERT INTO `" + Main.getMySQL().notificationTable + "`(`ID`,`OWNER`,`TYPE`,`DATA`) VALUES (?,?,?,?)";
            PreparedStatement stmt = Main.getMySQL().getConnection().prepareStatement(sql);
            stmt.setString(1, notificationId.toString());
            stmt.setString(2, playerId.toString());
            stmt.setString(3, type.toString());
            stmt.setString(4, data);
            stmt.execute();
            published = true;
        }
    }

    public void Delete() throws MNotification_NotPublished, SQLException {
        if (published) {
            String sql = "DELETE FROM `" + Main.getMySQL().notificationTable + "` WHERE `ID`=?";
            PreparedStatement stmt = Main.getMySQL().getConnection().prepareStatement(sql);
            stmt.setString(1, notificationId.toString());
            stmt.execute();
            published = false;
        } else {
            throw new MNotification_NotPublished();
        }
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NotifycationType getType() {
        return type;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getNotificationId() {
        return notificationId;
    }
}
