package me.trouper.ultrals;

import io.github.itzispyder.pdk.PDK;
import io.github.itzispyder.pdk.utils.misc.JsonSerializable;
import me.trouper.ultrals.cmds.*;
import me.trouper.ultrals.data.BankStorage;
import me.trouper.ultrals.data.HeartItemStorage;
import me.trouper.ultrals.data.MiscStorage;
import me.trouper.ultrals.data.config.Config;
import me.trouper.ultrals.events.ClickEvent;
import me.trouper.ultrals.events.DeathEvent;
import me.trouper.ultrals.events.PlayerEvents;
import me.trouper.ultrals.events.TeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class UltraLS extends JavaPlugin {

    private static UltraLS instance;
    private static final File cfgfile = new File("plugins/UltraLS/main-config.json");
    private static final File bankfile = new File("plugins/UltraLS/storage/banks.json");
    private static final File withdrawfile = new File("plugins/UltraLS/storage/heart-item-storage.json");
    private static final File miscfile = new File("plugins/UltraLS/storage/misc-storage.json");
    public static Config config = JsonSerializable.load(cfgfile, Config.class, new Config());
    public static MiscStorage misc = JsonSerializable.load(miscfile, MiscStorage.class, new MiscStorage());
    public static BankStorage bank = JsonSerializable.load(bankfile, BankStorage.class, new BankStorage());
    public static HeartItemStorage hearts = JsonSerializable.load(bankfile, HeartItemStorage.class, new HeartItemStorage());
    public static final Logger log = Bukkit.getLogger();

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {

        log.info("\n]======------ Pre-load started! ------======[");
        PDK.init(this);
        instance = this;

        log.info("Loading Config...");

        loadConfig();

        startup();
    }

    public void startup() {
        log.info("\n]======----- Loading UltraLS! -----======[");

        // Plugin startup logic
        log.info("Starting Up! (%s)...".formatted(getDescription().getVersion()));

        // Commands
        new UltraLSCommand().register();
        new GiveHeartCommand().register();
        new WithdrawCommand().register();
        new ReviveCommand().register();
        new DepositCommand().register();

        // Events
        new DeathEvent().register();
        new ClickEvent().register();
        new PlayerEvents().register();
        new TeleportEvent().register();

        log.info("""
                Finished!
                   _    _ _ _             _       _____\s
                  | |  | | | |           | |     / ____|
                  | |  | | | |_ _ __ __ _| |    | (___ \s
                  | |  | | | __| '__/ _` | |     \\___ \\\s
                  | |__| | | |_| | | (_| | |____ ____) |
                   \\____/|_|\\__|_|  \\__,_|______|_____/\s                    
                  ]==-- LifeSteal done correctly --==[""");
    }

    public void loadConfig() {

        // Init
        config = JsonSerializable.load(cfgfile, Config.class,new Config());
        bank = JsonSerializable.load(bankfile,BankStorage.class,new BankStorage());
        hearts = JsonSerializable.load(withdrawfile, HeartItemStorage.class,new HeartItemStorage());
        // Save
        config.save();
        config.save();
        bank.save();
    }


    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("UltraLS has disabled! (%s)".formatted(getDescription().getVersion()));
    }

    public static UltraLS getInstance() {
        return instance;
    }

}
