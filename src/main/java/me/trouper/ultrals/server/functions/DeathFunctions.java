package me.trouper.ultrals.server.functions;

import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class DeathFunctions {

    public static void revive(Player s, OfflinePlayer r) {
        if (isAlive(r)) {
            s.sendMessage(Text.prefix("You can only revive a dead player!"));
            return;
        }

        AttributeInstance whoa = s.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (whoa.getBaseValue() - UltraLS.config.plugin.heartsFromPlayers < UltraLS.config.plugin.minHP) {
            s.sendMessage(Text.prefix("&cYou do not have enough health to revive someone!"));
            return;
        }

        whoa.setBaseValue(whoa.getBaseValue() - UltraLS.config.plugin.heartsFromPlayers);
        UltraLS.misc.deadPlayers.remove(r.getUniqueId().toString());
        UltraLS.misc.save();
    }

    public static void permaKill(Player v, Player k) {
        UltraLS.misc.deadPlayers.add(v.getUniqueId().toString());
        UltraLS.misc.save();
        Bukkit.broadcast(Component.text(Text.prefix("&a%s&7 Has been permanently killed by &c%s&7!".formatted(
                v.getName(),
                k.getName()
        ))));
        Component message = Component.text(Text.color("&7&lYou have been killed by &c%s&7!"));
        if (UltraLS.config.plugin.deathSpectator) message = message.append(Component.text(Text.color("\n&aYou may rejoin to spectate")));
        v.kick(message);
    }


    public static boolean isAlive(OfflinePlayer p) {
        return !UltraLS.misc.deadPlayers.contains(p.getUniqueId().toString());
    }
}
