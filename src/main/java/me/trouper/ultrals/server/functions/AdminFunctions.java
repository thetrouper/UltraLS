package me.trouper.ultrals.server.functions;

import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminFunctions {

    public static boolean addHearts(CommandSender s, Player r, int amount, boolean anonymous) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cInvalid Input.&7 Could not subtract hearts from &c%s&7. You must provide a value above &e0&7."
                    .formatted(r.getName())
            ));
            return false;
        }
        AttributeInstance healthAttribute = r.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        int balance = UltraLS.bank.balances.get(r.getUniqueId().toString());
        double health = healthAttribute.getBaseValue();
        double compositeMax = UltraLS.config.bank.maxBalance + UltraLS.config.plugin.maxHP;
        double compositeHearts = balance + health;
        double emptyHearts = UltraLS.config.plugin.maxHP - health;
        double emptyBal = UltraLS.config.bank.maxBalance - balance;
        double compositeEmpty = emptyBal + emptyHearts;
        double leftToGive;

        if (compositeEmpty - amount < 1) {
            s.sendMessage(Text.prefix("&cOverflow error. &7Cannot add hearts to &e%s&7. Result would be over max health.".formatted(r.getName())));
            return false;
        }
        // Confirmed space is available.

        if (emptyHearts - amount < 1) {
            healthAttribute.setBaseValue(UltraLS.config.plugin.maxHP);
            leftToGive = amount - emptyHearts;
            UltraLS.bank.balances.put(r.getUniqueId().toString(), (int) (balance + Math.floor(leftToGive)));
            UltraLS.bank.save();
            String who = s.getName();
            if (anonymous) who = "Console";
            r.sendMessage(Text.prefix("&e%s&7 has added &a%s&7 hearts to your player.".formatted(who,amount)));
            s.sendMessage(Text.prefix("Successfully added &a%s&7 hearts to &e%s&7.".formatted(amount,r.getName())));
            return true;
        }

        healthAttribute.setBaseValue(health + amount);
        String who = s.getName();
        if (anonymous) who = "Console";
        r.sendMessage(Text.prefix("&e%s&7 has added &a%s&7 hearts to your player.".formatted(who,amount)));
        s.sendMessage(Text.prefix("Successfully added &a%s&7 hearts to &e%s&7.".formatted(amount,r.getName())));
        return true;
    }

    public static boolean subtractHearts(CommandSender s, Player r, int amount, boolean anonymous) {
        if (amount < 1) {
            s.sendMessage(Text.prefix("&cInvalid Input.&7 Could not subtract hearts from &c%s&7. You must provide a value above &e0&7."
                    .formatted(r.getName())
            ));
            return false;
        }
        AttributeInstance healthAttribute = r.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        int balance = UltraLS.bank.balances.get(r.getUniqueId().toString());
        double availableBarHealth = healthAttribute.getBaseValue() - UltraLS.config.plugin.minHP;
        double compositeHearts = balance + availableBarHealth;
        double leftToTake = amount;
        String who = s.getName();
        if (anonymous) who = "Console";

        if (compositeHearts < amount) {
            s.sendMessage(Text.prefix("&cInsufficient Balance. &7There are not enough total hearts on &e%s&7."));
            return false;
        }
        // There are enough total hearts

        if (amount > availableBarHealth) {
            // Take as much as possible from the bar, then update the left to take which will be subtracted from the bank
            healthAttribute.setBaseValue(UltraLS.config.plugin.minHP);
            leftToTake = amount - availableBarHealth;
            UltraLS.bank.balances.put(r.getUniqueId().toString(), (int) (balance - leftToTake));
            UltraLS.bank.save();
            r.sendMessage(Text.prefix("&e%s&7 has subtracted &c%s&7 hearts from your player.".formatted(who,amount)));
            s.sendMessage(Text.prefix("Successfully subtracted &c%s&7 hearts from &e%s&7.".formatted(amount,r.getName())));
            return true;
        }
        healthAttribute.setBaseValue(healthAttribute.getBaseValue() + amount);
        r.sendMessage(Text.prefix("&e%s&7 has subtracted &c%s&7 hearts from your player.".formatted(who,amount)));
        s.sendMessage(Text.prefix("Successfully subtracted &c%s&7 hearts from &e%s&7.".formatted(amount,r.getName())));
        return true;
    }

    public static boolean setHearts(CommandSender s, Player r, int amount, boolean anonymous) {
        if (amount > UltraLS.config.plugin.maxHP || amount < UltraLS.config.plugin.minHP) {
            s.sendMessage(Text.prefix("&cInvalid Input.&7 Could not set the hearts of &c%s&7. You must provide a value between &e%s&7 and &e%s&7."
                    .formatted(r.getName(),UltraLS.config.plugin.minHP,UltraLS.config.plugin.maxHP)
            ));
            return false;
        }
        AttributeInstance healthAttribute = r.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        String who = s.getName();
        if (anonymous) who = "Console";

        // Value is possible
        healthAttribute.setBaseValue(amount);
        r.sendMessage(Text.prefix("&e%s&7 has set your hearts to &a%s&7.".formatted(who,amount)));
        s.sendMessage(Text.prefix("Successfully set &e%s&7's hearts to &a%s&7.".formatted(r.getName(),amount)));
        return true;
    }

    public static boolean setBank(CommandSender s, OfflinePlayer r, int amount, boolean anonymous) {
        if (amount > UltraLS.config.bank.maxBalance || amount < 0) {
            s.sendMessage(Text.prefix("&cInvalid Input.&7 Could not set the hearts of &c%s&7. You must provide a value between &e%s&7 and &e%s&7."
                    .formatted(r.getName(),0,UltraLS.config.bank.maxBalance)
            ));
            return false;
        }
        String who = s.getName();
        if (anonymous) who = "Console";
        // Value is possible

        UltraLS.bank.balances.put(r.getUniqueId().toString(), amount);
        UltraLS.bank.save();
        if (r.isConnected()) r.getPlayer().sendMessage(Text.prefix("&e%s&7 has set your hearts to &a%s&7.".formatted(who,amount)));
        s.sendMessage(Text.prefix("Successfully set &e%s&7's hearts to &a%s&7.".formatted(r.getName(),amount)));
        return true;
    }

    public static void reset(CommandSender s, OfflinePlayer r, boolean anonymous) {
        UltraLS.bank.balances.remove(r.getUniqueId().toString());
        UltraLS.bank.save();
        String who = s.getName();
        if (anonymous) who = "Console";

        s.sendMessage(Text.prefix("Successfully reset &c%s&7.".formatted(r.getName())));
        if (r.isConnected()) {
            r.getPlayer().kick(Component.text(Text.color("&4&lHeart Reset\n&7Your hearts have been reset by &e%s&7.\n\nYou may now rejoin.".formatted(who))));
        }
    }
}
