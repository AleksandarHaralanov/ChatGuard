package io.github.aleksandarharalanov.chatguard.command.subcommand;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "chatguard.config", "[ChatGuard] You don't have permission to reload the config.")) {
            return true;
        }

        if (sender instanceof Player) {
            sender.sendMessage(ColorUtil.translateColorCodes("&a[ChatGuard] Configurations reloaded."));
        }
        LogUtil.logConsoleInfo("[ChatGuard] Configurations reloaded.");

        ChatGuard.getConfig().loadAndLog();
        ChatGuard.getDiscord().loadAndLog();
        ChatGuard.getStrikes().loadAndLog();
        ChatGuard.getCaptchas().loadAndLog();

        return true;
    }
}
