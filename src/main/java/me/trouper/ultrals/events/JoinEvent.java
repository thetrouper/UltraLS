package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements CustomListener {
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
}
