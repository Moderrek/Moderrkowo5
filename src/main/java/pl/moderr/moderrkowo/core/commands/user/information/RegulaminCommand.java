package pl.moderr.moderrkowo.core.commands.user.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class RegulaminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtils.color("&cRegulamin MODERRKOWO.PL"));
        sender.sendMessage(ColorUtils.color("&c1. &eNa serwerze jeden gracz może posiadać 1 działkę na danej mapie."));
        sender.sendMessage(ColorUtils.color("&c2. &eZakaz przeklinania oraz ubliżania innym graczom."));
        sender.sendMessage(ColorUtils.color("&c3. &eZakaz spamowania."));
        sender.sendMessage(ColorUtils.color("&c4. &eZakaz reklamowania innych serwerów (przykładowo, podawanie adresów IP, nazw itp)."));
        sender.sendMessage(ColorUtils.color("&c5. &eNie szanujemy griefowania/kradzieży."));
        sender.sendMessage(ColorUtils.color("&c6. &eZakaz wykorzystywania bugów gry/pluginów."));
        sender.sendMessage(ColorUtils.color("&c7. &eHandel z graczami odbywa się na własne ryzyko."));
        sender.sendMessage(ColorUtils.color("&c8. &eZakaz podawania się za członka administracji serwera oraz powoływania się na znajomości przez nieuprawnionych graczy."));
        sender.sendMessage(ColorUtils.color("&c9. &eZakazane są: nękanie innych graczy oraz groźby nie związane z grą."));
        sender.sendMessage(ColorUtils.color("&c10. &eZakaz pisania nagminnych próśb do administracji o przedmioty"));
        sender.sendMessage(ColorUtils.color("&c11. &eTak zwane TP-Kill'e są zakazane"));
        sender.sendMessage(ColorUtils.color("&c12. &eZakaz nadużywania dużych liter (Caps Lock)"));
        sender.sendMessage(ColorUtils.color("&c13. &eAdmin ma zawsze racje."));
        sender.sendMessage(ColorUtils.color("&c14. &eGranie na serwerze oznacza akceptację regulaminu!"));
        sender.sendMessage(ColorUtils.color("&c15. &eNie okradamy innych."));
        sender.sendMessage(ColorUtils.color("&c16. &eWykorzystywanie bugów w celu zysku ban"));
        sender.sendMessage(ColorUtils.color("&c15. &eMożna się handlować z osobą, ktora cię zabiła i ma twoje przedmioty"));
        return false;
    }
}
