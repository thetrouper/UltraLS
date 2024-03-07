package me.trouper.ultrals.cmds;

import io.github.itzispyder.pdk.commands.Args;
import io.github.itzispyder.pdk.commands.CommandRegistry;
import io.github.itzispyder.pdk.commands.CustomCommand;
import io.github.itzispyder.pdk.commands.completions.CompletionBuilder;
import me.trouper.ultrals.server.functions.BankFunctions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRegistry(value = "deposit",printStackTrace = true)
public class DepositCommand implements CustomCommand {
    @Override
    public void dispatchCommand(CommandSender sender, Args args) {
        Player p = (Player) sender;
        int amount = args.get(0).toInt();

        BankFunctions.depositHeart(p,amount);
    }

    @Override
    public void dispatchCompletions(CompletionBuilder b) {
        b.then(b.arg("<int>"));
    }
}
