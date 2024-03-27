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

@CommandRegistry(value = "bank",permission = @Permission(value = "ultrals.bank"), playersOnly = true)
public class BankCommand implements CustomCommand {
    @Override
    public void dispatchCommand(CommandSender commandSender, Args args) {
        commandSender.sendMessage(Text.prefix("Your balance is &a%s&7."
                .formatted(
                        UltraLS.bank.balances.get(((Player) (commandSender)).getUniqueId().toString())
                )));
    }

    @Override
    public void dispatchCompletions(CompletionBuilder completionBuilder) {

    }
}
