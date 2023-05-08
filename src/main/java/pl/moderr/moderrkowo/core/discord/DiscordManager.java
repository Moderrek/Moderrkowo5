package pl.moderr.moderrkowo.core.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.listeners.MotdListener;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;

public class DiscordManager extends ListenerAdapter {

    private final String token = Main.getInstance().getConfig().getString("discord-token");
    private final long moderrkowo = 920374927733960774L;
    private final long admin_chat = 920374982008266884L;
    private JDA jda;

    public DiscordManager() {

    }

    public void EndBot() {
        jda.shutdownNow();
    }

    public void StartBot() throws LoginException {
        Logger.logDiscordMessage("Autoryzowanie bota..");
        Main.getInstance().logger.log(Level.INFO, "Autoryzowanie bota z tokenem " + token);
        jda = JDABuilder.createDefault(token)
                .addEventListeners(
                        new DiscordJoinQuitListener(jda),
                        new ReadyListener()
                )
                .setActivity(Activity.playing("Moderrkowo.PL"))
                .build();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            Guild guild = jda.getGuildById(moderrkowo);
            assert guild != null;
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(920425673745653800L)).getManager().setName("\uD83D\uDC93 Rekord online: " + Main.getInstance().dataConfig.getInt("MaxPlayer")).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(920425628631728158L)).getManager().setName("\uD83D\uDFE2 Online: " + Bukkit.getOnlinePlayers().size()).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(920425595811282964L)).getManager().setName("\uD83C\uDFAE Zarejestrowanych: " + Main.getMySQL().getQuery().getCountOfUsers()).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 20 * 60 * 5);
        Logger.logDiscordMessage("Załadowano bota");
        if(LocalDateTime.now().isBefore(MotdListener.SERVER_START_DATE)){
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> jda.getPresence().setActivity(Activity.watching(ChatUtil.getTime(MotdListener.SERVER_START_DATE))), 0, 20 * 60);
        }
    }

    public void sendHelpop(Player p, String message, boolean b) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.BLUE);
        embedBuilder.setTitle("Wiadomość do administracji");
        embedBuilder.setFooter(p.getName());
        embedBuilder.addField("", message, false);
        embedBuilder.addField("Wer", Main.getVersion(), false);
        embedBuilder.addField("UUID", p.getUniqueId().toString(), false);
        if (b) {
            int i = (int) Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).count();
            embedBuilder.addField("Admin Online", i + "", false);
        } else {
            embedBuilder.addField("Admin Offline", "", false);
        }
        jda.getGuildById(moderrkowo).getTextChannelById(admin_chat).sendMessageEmbeds(embedBuilder.build()).queue();
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[DC] >> [PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        } else {
            if (event.getGuild().getIdLong() == moderrkowo) {
                System.out.printf("[DC] >> [%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
                        event.getMessage().getContentDisplay());

                if (event.getChannel().getIdLong() == 850252360009121812L) {
                    event.getMessage().delete().complete();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(ColorUtils.RED);
                    embedBuilder.setTitle("Zgłoszenie błędu");
                    embedBuilder.setFooter(event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
                    embedBuilder.addField("", event.getMessage().getContentDisplay(), false);
                    embedBuilder.addField("Wer", Main.getVersion(), false);
                    event.getGuild().getTextChannelById(850252515810344980L).sendMessageEmbeds(embedBuilder.build()).queue();
                }
            }
        }
    }

    public JDA getJda() {
        return jda;
    }

    public void sendTryJoin(String name) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.GOLD);
        embedBuilder.setTitle("Próba wejścia przed startem");
        embedBuilder.setFooter(ChatUtil.getTime(MotdListener.SERVER_START_DATE));
        embedBuilder.addField("", name, false);
        Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(moderrkowo)).getTextChannelById(admin_chat)).sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
