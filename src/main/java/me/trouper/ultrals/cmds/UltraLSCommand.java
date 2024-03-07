package me.trouper.ultrals.cmds;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.Permission;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRegistry(value = "ultrals", permission = @Permission("ultrals.admin"), printStackTrace = true)
public class UltraLSCommand implements CustomCommand {

    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player p = (Player) sender;

        switch (args.get(0).toString()) {
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
        b.then(b.arg("toggle")
                .then(b.arg("debug")));
    }

}
