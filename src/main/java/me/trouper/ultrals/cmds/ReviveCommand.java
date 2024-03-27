package me.trouper.ultrals.cmds;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.Permission;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.ServerUtils;
import me.trouper.ultrals.UltraLS;
import me.trouper.ultrals.server.functions.DeathFunctions;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandRegistry(value = "revive",permission = @Permission("ultrals.revive"),printStackTrace = true)
public class ReviveCommand implements CustomCommand {
    @Override
    public void dispatchCommand(CommandSender commandSender, Args args) {
        Player s = (Player) commandSender;
        OfflinePlayer r = Bukkit.getOfflinePlayer(args.get(0).toString());
        DeathFunctions.revive(s,r);
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        List<String> players = new ArrayList<>();
        for (Player player : ServerUtils.players()) {
            players.add(player.getName());
        }

        b.then(b.arg(players));
    }
}
