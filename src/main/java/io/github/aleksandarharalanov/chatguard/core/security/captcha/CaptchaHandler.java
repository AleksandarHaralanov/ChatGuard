package io.github.aleksandarharalanov.chatguard.core.security.captcha;

import io.github.aleksandarharalanov.chatguard.core.data.CaptchaData;
import io.github.aleksandarharalanov.chatguard.core.log.logger.ConsoleLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.DiscordLogger;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.misc.AudioCuePlayer;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class CaptchaHandler {

    private CaptchaHandler() {}

    public static boolean doesPlayerHaveActiveCaptcha(Player player) {
        String captchaCode = CaptchaData.getPlayerCaptcha(player.getName());
        if (captchaCode != null) {
            player.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] You have an active captcha verification."));
            player.sendMessage(ColorUtil.translateColorCodes(String.format(
                    "&cUse &e/cg captcha &b%s &cto verify. Case-sensitive!", captchaCode
            )));
            AudioCuePlayer.play(LogType.CAPTCHA, player, false);
            return true;
        }
        return false;
    }

    public static void processCaptchaTrigger(Player player, String content) {
        String captchaCode = CaptchaGenerator.generateCaptchaCode();
        CaptchaData.setPlayerCaptcha(player.getName(), captchaCode);

        player.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] Captcha verification triggered."));
        player.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&cUse &e/cg captcha &b%s &cto verify. Case-sensitive!", captchaCode
        )));

        AudioCuePlayer.play(LogType.CAPTCHA, player, false);
        ConsoleLogger.log(LogType.CAPTCHA, player, content);
        DiscordLogger.log(LogType.CAPTCHA, player, content, null);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (AccessUtil.senderHasPermission(p, "chatguard.captcha")) {
                p.sendMessage(ColorUtil.translateColorCodes(String.format(
                        "&c[ChatGuard] &e%s&c triggered captcha verification.", player.getName()
                )));
            }
        }
    }
}
