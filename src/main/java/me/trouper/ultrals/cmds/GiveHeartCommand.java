package me.trouper.ultrals.cmds;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import io.github.itzispyder.pdk.utils.ServerUtils;
import me.trouper.ultrals.server.functions.TransferFunctions;
import me.trouper.ultrals.server.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
@CommandRegistry(value = "giveheart",printStackTrace = true)
public class GiveHeartCommand implements CustomCommand {
    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player s = (Player) sender;
        Player r = Bukkit.getServer().getPlayer(args.get(0).toString());
        if (r == null) {
            s.sendMessage(Text.prefix("&cYou must provide an online player!&7 Use the /revive command to give hearts to a dead player."));
            return;
        }
        int amount = args.get(1).toInt();
        String to = args.get(2).toString();
        String from = args.get(3).toString();
        if (from.equals("bank")) {
            if (to.equals("bank")) {
                TransferFunctions.bankToBank(s,r,amount);
                return;
            }
            TransferFunctions.bankToBar(s,r,amount);
            return;
        }
        if (to.equals("bank")) {
            TransferFunctions.barToBank(s,r,amount);
        }
        TransferFunctions.barToBar(s,r,amount);

    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        List<String> players = new ArrayList<>();
        for (Player player : ServerUtils.players()) {
            players.add(player.getName());
        }

        b.then(b.arg(players)
                .then(b.arg("<int: hearts to send>")
                        .then(b.arg("[from: bar | bank]","bank","bar")
                                .then(b.arg("[to: bar | bank]","bank","bar")))));
    }
}
