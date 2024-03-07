package me.trouper.ultrals.server.functions;

import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.ServerUtils;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DeathFunctions {

    public static void unBan(OfflinePlayer p, String who) {
        ServerUtils.sendCommand(UltraLS.config.plugin.reviveCommand.formatted(p.getName()));
        Bukkit.broadcast(Component.text(Text.prefix("&a%s&7 has been revived by &e%s&7!".formatted(p.getName(),who))));
    }

    public static void ban(Player p) {
        ServerUtils.sendCommand(UltraLS.config.plugin.deathBanCommand.formatted(p.getName()));
        Bukkit.broadcast(Component.text(Text.prefix("&a%s&7 is now dead!".formatted(p.getName()))));
    }

    public static void unSpectator(Player p) {
        p.removeScoreboardTag("ULTRALS_dead");
        p.setGameMode(GameMode.SURVIVAL);
    }

    public static void spectator(Player p) {
        p.addScoreboardTag("ULTRALS_dead");
        p.setGameMode(GameMode.SPECTATOR);
    }
}
