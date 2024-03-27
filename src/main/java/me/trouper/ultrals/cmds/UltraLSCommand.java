package me.trouper.ultrals.cmds;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.Permission;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.ServerUtils;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.AdminFunctions;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandRegistry(value = "ultrals", permission = @Permission("ultrals.admin"), printStackTrace = true)
public class UltraLSCommand implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player p = (Player) sender;

        switch (args.get(0).toString()) {
            case "hearts" -> {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args.get(1).toString());

                switch (args.get(2).toString()) {
                    case "add" -> {
                        if (!target.isConnected()) {
                            p.sendMessage(Text.prefix("&cNull Error. &7Target is not online."));
                            return;
                        }
                        int amount = args.get(3).toInt();
                        AdminFunctions.addHearts(sender,target.getPlayer(),amount,args.get(4).toBool());
                    }
                    case "subtract" -> {
                        if (!target.isConnected()) {
                            p.sendMessage(Text.prefix("&cNull Error. &7Target is not online."));
                            return;
                        }
                        int amount = args.get(3).toInt();
                        AdminFunctions.subtractHearts(sender,target.getPlayer(),amount,args.get(4).toBool());
                    }
                    case "reset" -> {
                        AdminFunctions.reset(sender,target,args.get(3).toBool());
                    }
                    case "set" -> {
                        switch (args.get(3).toString()) {
                            case "bank" -> {
                                int amount = args.get(4).toInt();
                                AdminFunctions.setBank(sender,target,amount,args.get(5).toBool());
                            }
                            case "bar" -> {
                                int amount = args.get(4).toInt();
                                if (!target.isConnected()) {
                                    p.sendMessage(Text.prefix("&cNull Error. &7Target is not online."));
                                    return;
                                }
                                AdminFunctions.setHearts(sender,target.getPlayer(),amount,args.get(5).toBool());
                            }
                        }
                    }
                }
            }
            case "toggle" -> {
                switch (args.get(1).toString()) {
                    case "debug" -> {
                        UltraLS.config.debugMode = !UltraLS.config.debugMode;
                        UltraLS.config.save();
                        p.sendMessage(Text.prefix("Debug mode is now &a%s&7.".formatted(UltraLS.config.debugMode)));
                    }
                }
            }
        }
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        List<String> players = new ArrayList<>();
        for (Player player : ServerUtils.players()) {
            players.add(player.getName());
        }

        b.then(b.arg("toggle")
                .then(b.arg("debug"))
        ).then(b.arg("hearts")
                .then(b.arg(players)
                        .then(b.arg("add","remove")
                                .then(b.arg("<int>")
                                        .then(b.arg("<bool: anonymous>"))))
                        .then(b.arg("reset")
                                .then(b.arg("<bool: anonymous>")))
                        .then(b.arg("set")
                                .then(b.arg("bank","bar")
                                        .then(b.arg("<int>")
                                                .then(b.arg("<bool: anonymous>")
                                                )
                                        )
                                )
                        )
                )
        );
    }

}
