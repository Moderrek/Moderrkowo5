package pl.moderr.moderrkowo.core.user.notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.user.notification.exceptions.MNotification_CannotPublish;
import pl.moderr.moderrkowo.core.user.notification.exceptions.MNotification_NotPublished;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MNotification {

    private final UUID notificationId;
    private final UUID playerId;
    private final NotificationType type;
    @Setter(AccessLevel.NONE)
    private boolean published;
    private String data;

    public void publish() throws MNotification_CannotPublish, SQLException {
        if (published) throw new MNotification_CannotPublish();
        final String SQL = "INSERT INTO `" + ModerrkowoPlugin.getMySQL().notificationTable + "` (`ID`,`OWNER`,`TYPE`,`DATA`) VALUES (?,?,?,?)";
        final PreparedStatement statement = ModerrkowoPlugin.getMySQL().getConnection().prepareStatement(SQL);
        statement.setString(1, notificationId.toString());
        statement.setString(2, playerId.toString());
        statement.setString(3, type.toString());
        statement.setString(4, data);
        statement.execute();
        statement.close();
        published = true;
    }

    public void remove() throws MNotification_NotPublished, SQLException {
        if (!published) throw new MNotification_NotPublished();
        final String SQL = "DELETE FROM `" + ModerrkowoPlugin.getMySQL().notificationTable + "` WHERE `ID`=?";
        final PreparedStatement statement = ModerrkowoPlugin.getMySQL().getConnection().prepareStatement(SQL);
        statement.setString(1, notificationId.toString());
        statement.execute();
        statement.close();
        published = false;
    }

}
