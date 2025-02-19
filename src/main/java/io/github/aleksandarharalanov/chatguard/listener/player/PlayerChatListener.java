package io.github.aleksandarharalanov.chatguard.listener.player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.misc.TimeFormatter;
import io.github.aleksandarharalanov.chatguard.core.security.captcha.CaptchaDetector;
import io.github.aleksandarharalanov.chatguard.core.security.captcha.CaptchaHandler;
import io.github.aleksandarharalanov.chatguard.core.security.filter.ContentFilter;
import io.github.aleksandarharalanov.chatguard.core.security.spam.ChatRateLimiter;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isPlayerEssentialsMuted(player, event)) return;
        if (hasBypassPermission(player)) return;
        if (handleActiveCaptchaVerification(player, event)) return;
        if (handleSpamPrevention(player, event)) return;
        if (handleChatFiltering(player, event)) return;
        if (handleCaptchaTriggerCheck(player, event)) return;
    }

    private static boolean isPlayerEssentialsMuted(Player player, PlayerChatEvent event) {
        Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null && essentials.isEnabled()) {
            User user = essentials.getUser(player.getName());
            if (user.isMuted()) {
                TimeFormatter.printFormattedMuteDuration(user);
                event.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    private static boolean hasBypassPermission(Player player) {
        return AccessUtil.senderHasPermission(player, "chatguard.bypass");
    }

    private static boolean handleActiveCaptchaVerification(Player player, PlayerChatEvent event) {
        boolean isCaptchaEnabled = ChatGuard.getConfig().getBoolean("captcha.enabled", true);
        if (isCaptchaEnabled && CaptchaHandler.doesPlayerHaveActiveCaptcha(player)) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleSpamPrevention(Player player, PlayerChatEvent event) {
        boolean isChatSpamPreventionEnabled = ChatGuard.getConfig().getBoolean("spam-prevention.enabled.chat", true);
        if (isChatSpamPreventionEnabled && ChatRateLimiter.isPlayerChatSpamming(player)) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleChatFiltering(Player player, PlayerChatEvent event) {
        boolean isChatFilterEnabled = ChatGuard.getConfig().getBoolean("filter.enabled.chat", true);
        if (isChatFilterEnabled && ContentFilter.isChatContentBlocked(player, event.getMessage())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleCaptchaTriggerCheck(Player player, PlayerChatEvent event) {
        boolean isCaptchaEnabled = ChatGuard.getConfig().getBoolean("captcha.enabled", true);
        if (isCaptchaEnabled && CaptchaDetector.doesPlayerTriggerCaptcha(player.getName(), event.getMessage())) {
            CaptchaHandler.processCaptchaTrigger(player, event.getMessage());
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
