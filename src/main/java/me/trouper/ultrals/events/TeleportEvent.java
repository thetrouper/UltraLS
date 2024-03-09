package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.DeathFunctions;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvent implements CustomListener {

    @EventHandler
    public void onSpectatorTeleport(PlayerTeleportEvent e) {
        if (DeathFunctions.isAlive(e.getPlayer())) return;
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE) && !UltraLS.config.plugin.preventSpectatorTeleport) return;
        e.getPlayer().sendMessage(Text.prefix("Spectator teleportation has been disabled!"));
        e.setCancelled(true);
    }
}
