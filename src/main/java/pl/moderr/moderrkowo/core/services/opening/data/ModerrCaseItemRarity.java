package pl.moderr.moderrkowo.core.services.opening.data;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum ModerrCaseItemRarity {
    POSPOLITE(Component.text("P").decoration(TextDecoration.BOLD, true).color(NamedTextColor.WHITE)),
    RZADKIE(Component.text("R").decoration(TextDecoration.BOLD, true).color(TextColor.color(0x00DAFF))),
    LEGENDARNE(Component.text("L").decoration(TextDecoration.BOLD, true).color(TextColor.color(0xE770FF))),
    MITYCZNE(Component.text("M").decoration(TextDecoration.BOLD, true).color(TextColor.color(0xFF8900))),
    ZALAMANY(Component.text("Z").decoration(TextDecoration.BOLD, true).color(NamedTextColor.WHITE));

    @Getter
    private final Component prefix;

    ModerrCaseItemRarity(Component prefix) {
        this.prefix = prefix;
    }
}
