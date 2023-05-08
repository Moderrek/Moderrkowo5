package pl.moderr.moderrkowo.core.discord;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.Logger;

public class ReadyListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if(genericEvent instanceof ReadyEvent)
            Logger.logDiscordMessage("Bot został uruchomiony");
    }
}
