package io.github.aleksandarharalanov.chatguard.command;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.command.subcommand.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public final class ChatGuardCommand implements CommandExecutor {

    private final Map<String, CommandExecutor> subcommands = new HashMap<>();

    public ChatGuardCommand(ChatGuard plugin) {
        subcommands.put("about", new AboutCommand(plugin));
        subcommands.put("reload", new ReloadCommand());
        subcommands.put("strike", new StrikeCommand());
        subcommands.put("captcha", new CaptchaCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(sender);
            return true;
        }

        CommandExecutor subcommand = subcommands.get(args[0].toLowerCase());
        if (subcommand != null) {
            return subcommand.onCommand(sender, command, label, args);
        }

        HelpCommand.sendHelp(sender);
        return true;
    }
}
