package io.github.aleksandarharalanov.chatguard.command.subcommand;

import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class HelpCommand {

    public static void sendHelp(CommandSender sender) {
        String[] messages = {
                "&bChatGuard commands:",
                "&e/cg &7- Displays this content.",
                "&e/cg about &7- About ChatGuard.",
                "&e/cg captcha <code> &7- Captcha verification.",
                "&bChatGuard staff commands:",
                "&e/cg reload &7- Reload ChatGuard config.",
                "&e/cg strike <username> &7- View strike of player.",
                "&e/cg strike <username> [0-5] &7- Set strike of player."
        };

        for (String message : messages) {
            if (sender instanceof Player) {
                sender.sendMessage(ColorUtil.translateColorCodes(message));
            } else {
                LogUtil.logConsoleInfo(message.replaceAll("&.", ""));
            }
        }
    }
}
