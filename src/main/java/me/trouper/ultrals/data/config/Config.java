package me.trouper.ultrals.data.config;

import io.github.itzispyder.pdk.utils.misc.JsonSerializable;

import java.io.File;

public class Config implements JsonSerializable<Config> {

    @Override
    public File getFile() {
        File file = new File("plugins/UltraLS/main-config.json");
        file.getParentFile().mkdirs();
        return file;
    }

    public String prefix = "&9UltraLS> &7";
    public boolean debugMode = false;
    public Plugin plugin = new Plugin();
    public Bank bank = new Bank();
    public NoobProtection noobProtection = new NoobProtection();

    public class Plugin {
        public int maxHP = 40; // maximum hearts a player can have
        public int minHP = 2; // Minimum harts a player can have without taking death action. Setting to 0 is best if you plan on spectator mode-ing or banning people.
        public int startingHP = 20; // The amount of HP you start with on first join
        public boolean deathSpectator = false; // make the player a spectator/ghost when they die
        public boolean deathBan = true; // Run the death ban command then they die
        public boolean preventSpectatorTeleport = true; // Prevents players from using the spectator menu to leaka bases
        public String deathBanCommand = "ban %s You have died!"; // command to be executed when death ban mode is enabled
        public String reviveCommand = "pardon %s"; // command to be executed to undo a death ban
        public int heartsFromEnvironment = 0; // Hearts lost from mobs/environmental damage such as lava or fall.
        public int heartsFromPlayers = 2; // Hearts gained/lost through PvP
        public int heartModelData = 6942;
    }

    public class Bank {
        public int startingBalance = 0; // The amount of hearts that you start with in the bank
        public int maxBalance = 120; // The maximum balance a player can hold
        public boolean stealDirectlyFromBank = true; // Weather to take hearts from the bank before the health bar
        public boolean siphonFromBank = true; // If a player has hearts in their bank, but not in their health bar, hearts will be taken from the bank instead of banning them.
        public boolean overflowToBank = true; // If the player is at max hearts, hearts they steal will get stored in their bank
    }

    public class NoobProtection {
        public int minutesPlayedToGiveHearts = 2; // How much playtime a player must have before they are allowed to give people hearts
        public int minutesPlayedToWithdrawItemHearts = 2;// How much playtime a player must have to take out heart items
    }
}
