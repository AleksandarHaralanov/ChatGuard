package io.github.aleksandarharalanov.chatguard.command.subcommand;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.CaptchaData;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.core.misc.AudioCuePlayer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CaptchaCommand implements CommandExecutor {

    private final ChatGuard plugin;

    public CaptchaCommand(ChatGuard plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (AccessUtil.denyIfNotPlayer(sender, plugin)) return true;

        if (args.length < 2) {
            sender.sendMessage(ColorUtil.translateColorCodes("&cUsage: /cg captcha <code>"));
            return true;
        }

        Player player = (Player) sender;
        String captchaCode = CaptchaData.getPlayerCaptcha(player.getName());

        if (captchaCode == null) {
            player.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] No active captcha verification."));
            return true;
        }

        if (!args[1].equals(captchaCode)) {
            player.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] Incorrect captcha code."));
            AudioCuePlayer.play(LogType.CAPTCHA, player, false);
            return true;
        }

        player.sendMessage(ColorUtil.translateColorCodes("&a[ChatGuard] Captcha verification passed."));
        CaptchaData.removePlayerCaptcha(player.getName());

        AudioCuePlayer.play(LogType.CAPTCHA, player, true);
        LogUtil.logConsoleInfo(String.format("[ChatGuard] Player '%s' passed captcha verification.", player.getName()));

        return true;
    }
}
