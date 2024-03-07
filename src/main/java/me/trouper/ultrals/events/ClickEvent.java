package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.BankFunctions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickEvent implements CustomListener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ItemStack i = e.getItem();
        if (!i.isEmpty() &&
                i.hasItemMeta() &&
                i.getItemMeta().hasCustomModelData()
                && i.getItemMeta().hasLore()
                && i.getItemMeta().getCustomModelData() != UltraLS.config.plugin.heartModelData) return;

        switch (e.getAction()) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> BankFunctions.depositItem(e.getPlayer(),false);
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> BankFunctions.depositItem(e.getPlayer(),true);
        }
    }
}
