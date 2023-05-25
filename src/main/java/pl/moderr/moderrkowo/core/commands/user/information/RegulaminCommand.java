package pl.moderr.moderrkowo.core.commands.user.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

public class RegulaminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtil.color("&cRegulamin MODERRKOWO.PL"));
        sender.sendMessage(ColorUtil.color("&c1. &eNa serwerze jeden gracz może posiadać 1 działkę na danej mapie."));
        sender.sendMessage(ColorUtil.color("&c2. &eZakaz przeklinania oraz ubliżania innym graczom."));
        sender.sendMessage(ColorUtil.color("&c3. &eZakaz spamowania."));
        sender.sendMessage(ColorUtil.color("&c4. &eZakaz reklamowania innych serwerów (przykładowo, podawanie adresów IP, nazw itp)."));
        sender.sendMessage(ColorUtil.color("&c5. &eNie szanujemy griefowania/kradzieży."));
        sender.sendMessage(ColorUtil.color("&c6. &eZakaz wykorzystywania bugów gry/pluginów."));
        sender.sendMessage(ColorUtil.color("&c7. &eHandel z graczami odbywa się na własne ryzyko."));
        sender.sendMessage(ColorUtil.color("&c8. &eZakaz podawania się za członka administracji serwera oraz powoływania się na znajomości przez nieuprawnionych graczy."));
        sender.sendMessage(ColorUtil.color("&c9. &eZakazane są: nękanie innych graczy oraz groźby nie związane z grą."));
        sender.sendMessage(ColorUtil.color("&c10. &eZakaz pisania nagminnych próśb do administracji o przedmioty"));
        sender.sendMessage(ColorUtil.color("&c11. &eTak zwane TP-Kill'e są zakazane"));
        sender.sendMessage(ColorUtil.color("&c12. &eZakaz nadużywania dużych liter (Caps Lock)"));
        sender.sendMessage(ColorUtil.color("&c13. &eAdmin ma zawsze racje."));
        sender.sendMessage(ColorUtil.color("&c14. &eGranie na serwerze oznacza akceptację regulaminu!"));
        sender.sendMessage(ColorUtil.color("&c15. &eNie okradamy innych."));
        sender.sendMessage(ColorUtil.color("&c16. &eWykorzystywanie bugów w celu zysku ban"));
        sender.sendMessage(ColorUtil.color("&c15. &eMożna się handlować z osobą, ktora cię zabiła i ma twoje przedmioty"));
        return false;
    }
}
