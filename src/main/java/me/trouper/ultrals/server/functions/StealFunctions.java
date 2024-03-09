package me.trouper.ultrals.server.functions;

import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class StealFunctions {

    public static void stealHeart(Player v, Player k) {
        // If set to steal from bank first, do that
        if (UltraLS.config.bank.stealDirectlyFromBank) {
            stealFromBank(v,k,true);
            return;
        }
        stealFromBar(v,k,UltraLS.config.bank.siphonFromBank);
    }

    public static void stealFromBank(Player v, Player k, boolean defaultBar) {
        // Start at both player's bank
        // Check that k is not max, and that v has hearts
        // if v does not have hearts, steal from bar
        int vb = UltraLS.bank.balances.getOrDefault(v.getUniqueId().toString(),0);
        int kb = UltraLS.bank.balances.getOrDefault(k.getUniqueId().toString(),0);

        if (kb +  UltraLS.config.plugin.heartsFromPlayers > UltraLS.config.bank.maxBalance) {
            // Killer does not have space for hearts in the bank
            if (defaultBar) {
                k.sendMessage(Text.prefix("Your bank is full! Hearts will be added to your bar"));
                stealFromBar(v,k,false);
                return;
            }
            k.sendMessage(Text.prefix("Your bank is full!"));
            return;
        }

        if (vb - UltraLS.config.plugin.heartsFromPlayers <= 0) {
            // Victim does not have any hearts in the bank
            if (defaultBar) {
                v.sendMessage(Text.prefix("Your bank was empty! Hearts will been taken from your bar."));
                stealFromBar(v,k,false);
                return;
            }
            v.sendMessage(Text.prefix("Your bank was empty!"));
            return;
        }

        UltraLS.bank.balances.put(v.getUniqueId().toString(), vb - UltraLS.config.plugin.heartsFromPlayers);
        UltraLS.bank.balances.put(k.getUniqueId().toString(), kb + UltraLS.config.plugin.heartsFromPlayers);

        UltraLS.bank.save();

        k.sendMessage(Text.prefix("You stole &a%s&7 heart(s) from &e%s&7.".formatted(UltraLS.config.plugin.heartsFromPlayers,v.getName())));
        v.sendMessage(Text.prefix("%s stole &a%s&7 heart(s) from you.".formatted(k.getName(),UltraLS.config.plugin.heartsFromPlayers)));
    }

    public static void stealFromBar(Player v, Player k, boolean defaultBank) {
        // Start at both player's attributes
        // Check that k is not max, and that v has enough hearts
        // if v does not have enough hearts, steal from bank
        AttributeInstance vh = v.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance kh = k.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (kh.getBaseValue() + UltraLS.config.plugin.heartsFromPlayers > UltraLS.config.plugin.maxHP) {
            // Killer does not have space for hearts
            if (defaultBank) {
                k.sendMessage(Text.prefix("Your health bar is full! Hearts will be added to your bank."));
                stealFromBank(v,k,false);
                return;
            }
            k.sendMessage(Text.prefix("Your health bar is full!"));
            return;
        }

        if (vh.getBaseValue() - UltraLS.config.plugin.heartsFromPlayers <= UltraLS.config.plugin.minHP && !UltraLS.config.plugin.deathBan && !UltraLS.config.plugin.deathSpectator) {
            // Victim does not have any hearts in their health bar, and the plugin is set to not spectator, or not ban them
            if (defaultBank) {
                v.sendMessage("You don't have enough hearts in your health bar! Hearts will been taken from your bank.");
                stealFromBank(v,k,false);
                return;
            }
            v.sendMessage("Your health bar does not have enough hearts to be stolen from.");
            return;
        } else if (vh.getBaseValue() - UltraLS.config.plugin.heartsFromPlayers <= UltraLS.config.plugin.minHP ) {
            DeathFunctions.permaKill(v,k);
            return;
        }

        vh.setBaseValue(vh.getBaseValue() - UltraLS.config.plugin.heartsFromPlayers);
        kh.setBaseValue(kh.getBaseValue() + UltraLS.config.plugin.heartsFromPlayers);

        k.sendMessage(Text.prefix("You stole &a%s&7 heart(s) from &e%s&7.".formatted(UltraLS.config.plugin.heartsFromPlayers,v.getName())));
        v.sendMessage(Text.prefix("&c%s&7 stole &a%s&7 heart(s) from you.".formatted(k.getName(),UltraLS.config.plugin.heartsFromPlayers)));
    }
}
