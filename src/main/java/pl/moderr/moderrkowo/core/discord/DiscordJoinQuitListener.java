package pl.moderr.moderrkowo.core.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class DiscordJoinQuitListener extends ListenerAdapter {

    JDA jda;

    public DiscordJoinQuitListener(JDA jda) {
        this.jda = jda;
    }

    @SubscribeEvent
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Logger.logDiscordMessage(event.getMember().getEffectiveName() + " opuścił");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.BLUE);
        embedBuilder.setDescription("**" + event.getMember().getNickname() + "** opuścił nas :c");
        embedBuilder.setAuthor(event.getMember().getNickname(), event.getMember().getUser().getAvatarUrl());
        event.getGuild().getTextChannelById(859418069444198401L).sendMessageEmbeds(embedBuilder.build()).queue();
    }


    @SubscribeEvent
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Logger.logDiscordMessage(event.getMember().getEffectiveName() + " dołączył");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.GREEN);
        embedBuilder.setDescription("**" + event.getMember().getNickname() + "** dołączył do Moderrkowa!");
        embedBuilder.setAuthor(event.getMember().getNickname(), event.getMember().getUser().getAvatarUrl());
        event.getGuild().getTextChannelById(859418069444198401L).sendMessageEmbeds(embedBuilder.build()).queue();
        event.getGuild().addRoleToMember(event.getMember(), jda.getRoleById(827531126197321738L)).queue();
    }

}
