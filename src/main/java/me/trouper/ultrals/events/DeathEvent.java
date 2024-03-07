package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.server.functions.StealFunctions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements CustomListener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        StealFunctions.stealHeart(e.getPlayer(),e.getEntity().getKiller());
    }


}
