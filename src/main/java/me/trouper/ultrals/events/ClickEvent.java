package me.trouper.ultrals.events;

import io.github.itzispyder.pdk.events.CustomListener;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.BankFunctions;
import me.trouper.ultrals.server.util.ServerUtils;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickEvent implements CustomListener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ItemStack i = e.getItem();
        ServerUtils.verbose("Click ");
        if (i == null) return;
        ServerUtils.verbose("Click 0");
        if (!i.getType().equals(Material.RED_DYE)) return;
        ServerUtils.verbose("Click 2");
        if (!i.isEmpty() &&
                i.hasItemMeta() &&
                i.getItemMeta().hasCustomModelData()
                && i.getItemMeta().hasLore()
                && i.getItemMeta().getCustomModelData() != UltraLS.config.plugin.heartModelData) return;
        ServerUtils.verbose("Click 2");

        if (i.getLore() == null) return;
        ServerUtils.verbose("Click 3");

        if (!i.getItemMeta().hasDisplayName()) return;

        ServerUtils.verbose("Click 4");

        switch (e.getAction()) {
            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> BankFunctions.depositItem(e.getPlayer(),false);
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> BankFunctions.depositItem(e.getPlayer(),true);
        }
    }
}
