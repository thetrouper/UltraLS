package me.trouper.ultrals.server.functions;

import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class TransferFunctions {

    public static void bankToBank(Player s, Player r, int amount) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cData validation error. Input value above 0"));
            return;
        }
        int sb = UltraLS.bank.balances.get(s.getUniqueId().toString());
        int rb = UltraLS.bank.balances.get(r.getUniqueId().toString());

        if (sb - amount < 0) {
            s.sendMessage(Text.prefix("&cInsufficient Balance!&7 You only have &e%s&7 hearts in your bank.".formatted(sb)));
            return;
        }

        if (rb + amount > UltraLS.config.bank.maxBalance) {
            s.sendMessage(Text.prefix("&cCould not wire hearts! &e%s&7's bank would overflow.".formatted(r.getName())));
            r.sendMessage(Text.prefix("&e%s&7 tried to wire &c%s&7 hearts to you, but your bank was full!".formatted(s.getName(),amount)));
            return;
        }

        UltraLS.bank.balances.put(s.getUniqueId().toString(),sb - amount);
        UltraLS.bank.balances.put(r.getUniqueId().toString(),rb + amount);

        s.sendMessage(Text.prefix("&7You have wired &a%s&7 hearts to &e%s&7. Your new balance is &a%s&7."
                .formatted(amount,
                        r.getName(),
                        UltraLS.bank.balances.get(s.getUniqueId().toString()))));
        r.sendMessage(Text.prefix("&e%s&7 has wired &a%s&7 hearts to you. Your new balance is &a%s&7."
                .formatted(s.getName(),
                        amount,
                        UltraLS.bank.balances.get(r.getUniqueId().toString()))));
    }

    public static void bankToBar(Player s, Player r, int amount) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cData validation error. Input value above 0"));
            return;
        }
        int sb = UltraLS.bank.balances.get(s.getUniqueId().toString());
        AttributeInstance ra = r.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (sb - amount < 0) {
            s.sendMessage(Text.prefix("&cInsufficient Balance!&7 You only have &e%s&7 hearts in your bank.".formatted(sb)));
            return;
        }

        if (ra.getBaseValue() + amount > UltraLS.config.plugin.maxHP) {
            s.sendMessage(Text.prefix("&cCould not transfer hearts! &e%s&7's health bar would overflow.".formatted(r.getName())));
            r.sendMessage(Text.prefix("&e%s&7 tried to transfer &c%s&7 hearts to you, but your health bar was full!".formatted(s.getName(),amount)));
            return;
        }

        UltraLS.bank.balances.put(s.getUniqueId().toString(),sb - amount);
        ra.setBaseValue(ra.getBaseValue() + amount);

        s.sendMessage(Text.prefix("&7You have transferred &a%s&7 hearts to &e%s&7. Your new balance is &a%s&7."
                .formatted(amount,
                        r.getName(),
                        UltraLS.bank.balances.get(s.getUniqueId().toString())
                )));
        r.sendMessage(Text.prefix("&e%s&7 has transferred &a%s&7 hearts to you."
                .formatted(s.getName(),
                        amount
                )));
    }

    public static void barToBank(Player s, Player r, int amount) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cData validation error. Input value above 0"));
            return;
        }
        int rb = UltraLS.bank.balances.get(r.getUniqueId().toString());
        AttributeInstance sa = s.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (sa.getBaseValue() - amount < UltraLS.config.plugin.minHP) {
            s.sendMessage(Text.prefix("&cInsufficient Hearts.&7 You would run out and die!"));
            return;
        }

        if (rb + amount > UltraLS.config.bank.maxBalance) {
            s.sendMessage(Text.prefix("&cCould not transfer hearts! &e%s&7's bank would overflow.".formatted(r.getName())));
            r.sendMessage(Text.prefix("&e%s&7 tried to transfer &c%s&7 hearts to you, but your bank was full!".formatted(s.getName(),amount)));
            return;
        }

        sa.setBaseValue(sa.getBaseValue() - amount);
        UltraLS.bank.balances.put(s.getUniqueId().toString(),rb + amount);

        s.sendMessage(Text.prefix("&7You have transferred &a%s&7 hearts to &e%s&7."
                .formatted(amount,
                        r.getName()
                )));
        r.sendMessage(Text.prefix("&e%s&7 has transferred &a%s&7 hearts to you. You new balance is &a%s&7."
                .formatted(s.getName(),
                        amount,
                        UltraLS.bank.balances.get(r.getUniqueId().toString())
                )));
    }

    public static void barToBar(Player s, Player r, int amount) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cData validation error. Input value above 0"));
            return;
        }
        AttributeInstance ra = r.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance sa = s.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (sa.getBaseValue() - amount < UltraLS.config.plugin.minHP) {
            s.sendMessage(Text.prefix("&cInsufficient Hearts.&7 You would run out and die!"));
            return;
        }

        if (ra.getBaseValue() + amount > UltraLS.config.plugin.maxHP) {
            s.sendMessage(Text  .prefix("&cCould not give &e%s&7 hearts! Their health bar would overflow.".formatted(r.getName())));
            r.sendMessage(Text.prefix("&e%s&7 tried to give &c%s&7 hearts to you, but your health bar was full!".formatted(s.getName(),amount)));
            return;
        }

        sa.setBaseValue(sa.getBaseValue() - amount);
        ra.setBaseValue(ra.getBaseValue() + amount);

        s.sendMessage(Text.prefix("&7You have given &a%s&7 hearts to &e%s&7."
                .formatted(amount,
                        r.getName()
                )));
        r.sendMessage(Text.prefix("&e%s&7 has given &a%s&7 hearts to you."
                .formatted(s.getName(),
                        amount
                )));
    }
}
