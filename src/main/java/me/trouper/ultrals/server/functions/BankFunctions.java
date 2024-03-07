package me.trouper.ultrals.server.functions;

import io.github.itzispyder.pdk.Global;
import io.github.itzispyder.pdk.plugin.builders.ItemBuilder;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.data.Items;
import me.trouper.ultrals.server.util.ServerUtils;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class BankFunctions {

    public static void withdraw(Player p, int amount) {
        int bal = UltraLS.bank.balances.get(p.getUniqueId().toString());
        AttributeInstance h = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (bal - amount < 0) {
            p.sendMessage(Text.prefix("&cInsufficient Balance. &7You only have &c%s&7 hearts in your bank.".formatted(bal)));
            return;
        }
        if (h.getBaseValue() + amount > UltraLS.config.plugin.maxHP) {
            p.sendMessage(Text.prefix("&cInsufficient Space. &7You would go over the max health. (&e%s&7)".formatted(UltraLS.config.plugin.maxHP)));
            return;
        }

        UltraLS.bank.balances.put(p.getUniqueId().toString(), bal - amount);
        UltraLS.bank.save();
        h.setBaseValue(h.getBaseValue() + amount);

        p.sendMessage(Text.prefix("&7Withdrew &a%s&7 hearts from your bank. Your balance is now &e%s&7.".formatted(amount,bal - amount)));
    }

    public static void withdrawItem(Player p, int amount) {
        int bal = UltraLS.bank.balances.get(p.getUniqueId().toString());

        if (bal - amount < 0) {
            p.sendMessage(Text.prefix("&cInsufficient Balance. &7You only have &c%s&7 hearts in your bank.".formatted(bal)));
            return;
        }

        UltraLS.bank.balances.put(p.getUniqueId().toString(), bal - amount);
        UltraLS.bank.save();

        UUID withdrawID = UUID.randomUUID();

        ServerUtils.verbose("UUID of Withdrawn item: %s".formatted(withdrawID.toString()));

        UltraLS.hearts.withdrawn.put(withdrawID.toString(),amount);
        UltraLS.hearts.save();

        ItemStack heartItem = ItemBuilder.create()
                .material(Material.RED_DYE)
                .name(Global.instance.color("&c%s &c&lHearts".formatted(amount)))
                .lore(Global.instance.color("&4➥&7 (Right-Click) Deposit to bank"))
                .lore(Global.instance.color("&4➥&7 (Left-Click) Deposit to health bar"))
                .lore("")
                .lore(Global.instance.color("&7ID: &8%s".formatted(withdrawID.toString())))
                .lore(Global.instance.color("&7Owner: &8%s".formatted(p.getName())))
                .enchant(Enchantment.LURE,1)
                .customModelData(UltraLS.config.plugin.heartModelData)
                .flag(ItemFlag.HIDE_ENCHANTS)
                .build();

        p.getInventory().addItem(heartItem);
        p.sendMessage(Text.prefix("&7Withdrew &c%s&7 hearts.".formatted(amount)));
    }

    public static void depositItem(Player p, boolean toBank) {
        ItemStack i = p.getInventory().getItemInMainHand();

        if (i.hasItemMeta() && i.getItemMeta().hasCustomModelData() && i.getItemMeta().hasLore() && i.getItemMeta().getCustomModelData() != UltraLS.config.plugin.heartModelData) {
            p.sendMessage(Text.prefix("&cThat item is not a heart!"));
            return;
        }

        String idString = "NOT HEART";

        for (String s : i.getLore()) {
            ServerUtils.verbose(s);
            if (s.contains(Text.color("&7ID: &8"))) {
                s = s.replaceAll(Text.color("&7ID: &8"),"");
                idString = s;
            }
        }

        if (idString.equals("NOT HEART")) {
            p.sendMessage(Text.prefix("&cThat ID is invalid!"));
            return;
        }

        ServerUtils.verbose("UUID of Deposit item: %s".formatted(idString));
        UUID typeID = UUID.fromString(idString);

        if (!UltraLS.hearts.withdrawn.containsKey(typeID.toString())) {
            p.sendMessage(Text.prefix("&cThat heart has already been redeemed!"));
            return;
        }

        int amount = UltraLS.hearts.withdrawn.get(typeID.toString());

        p.getInventory().removeItem(i);
        UltraLS.hearts.withdrawn.remove(typeID.toString());
        UltraLS.hearts.save();


        if (toBank) {
            addToBank(p,amount);
        } else {
            addToBar(p,amount);
        }
    }

    public static void depositHeart(Player p, int amount) {
        AttributeInstance h = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (h.getBaseValue() - amount < UltraLS.config.plugin.minHP) {
            p.sendMessage(Text.prefix("&cInsufficient Health. &7You would go below the minimum health and die! (&e%s&7)"
                    .formatted(UltraLS.config.plugin.minHP)
            ));
            return;
        }

        h.setBaseValue(h.getBaseValue() - amount);
        addToBank(p,amount);
    }

    public static void addToBar(Player p, int amount) {
        AttributeInstance h = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (h.getBaseValue() + amount > UltraLS.config.plugin.maxHP) {
            p.sendMessage(Text.prefix("&cInsufficient Space. &7You would go over the maximum health. (&e%s&7)"
                    .formatted(UltraLS.config.plugin.maxHP)
            ));
            return;
        }

        h.setBaseValue(h.getBaseValue() + amount);

        p.sendMessage(Text.prefix("Deposited &c%s&7 hearts into your health bar."
                .formatted(amount)
        ));
    }

    public static void addToBank(Player p, int amount) {
        if (UltraLS.bank.balances.get(p.getUniqueId().toString()) + amount > UltraLS.config.bank.maxBalance) {
            p.sendMessage(Text.prefix("&cInsufficient Space. &7You would go over the maximum balance. (&e%s&7)"
                    .formatted(UltraLS.config.bank.maxBalance)
            ));
            return;
        }

        UltraLS.bank.balances.put(p.getUniqueId().toString(), UltraLS.bank.balances.get(p.getUniqueId().toString()) + amount);
        UltraLS.bank.save();

        p.sendMessage(Text.prefix("Deposited &c%s&7 hearts into your heart bank. Your new balance is &c%s&7."
                .formatted(amount,UltraLS.bank.balances.get(p.getUniqueId().toString()))
        ));
    }
}
