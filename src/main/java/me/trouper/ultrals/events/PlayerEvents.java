package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.DeathFunctions;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements CustomListener {
    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (UltraLS.bank.balances.containsKey(p.getUniqueId().toString())) return;
        UltraLS.bank.balances.put(p.getUniqueId().toString(),UltraLS.config.bank.startingBalance);
        p.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(UltraLS.config.plugin.startingHP);
        UltraLS.bank.save();
        p.sendMessage(Component.text(Text.prefix("Welcome! Your HeartBank has been initialized correctly.")));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (DeathFunctions.isAlive(p)) return;

        if (UltraLS.config.plugin.deathBan) {
            e.joinMessage(Component.text(""));
            p.kick(Component.text(Text.color("&c&lYou are dead!\n&7Have someone to revive you or wait for an unban wave")));
            return;
        }

        p.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (DeathFunctions.isAlive(p)) return;
        if (UltraLS.config.plugin.deathBan) {
            e.quitMessage(Component.text(""));
        }
    }
}
