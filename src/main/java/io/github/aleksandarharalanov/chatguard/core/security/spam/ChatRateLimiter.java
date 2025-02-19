package io.github.aleksandarharalanov.chatguard.core.security.spam;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import io.github.aleksandarharalanov.chatguard.core.data.TimestampData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class ChatRateLimiter {

    private ChatRateLimiter() {}

    public static boolean isPlayerChatSpamming(Player player) {
        long timestamp = System.currentTimeMillis();
        String playerName = player.getName();

        long lastTimestamp = TimestampData.getMessageTimestamp(playerName);

        if (lastTimestamp != 0) {
            long elapsed = timestamp - lastTimestamp;
            int cooldown = ChatGuard.getConfig().getInt(String.format(
                    "spam-prevention.cooldown-ms.chat.s%d", PenaltyData.getStrike(player)
            ), 0);

            if (elapsed <= cooldown) {
                boolean isWarnEnabled = ChatGuard.getConfig().getBoolean("spam-prevention.warn-player", true);
                if (isWarnEnabled) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&cPlease wait %.2f sec. before sending another message.", remainingTime
                    )));
                }
                return true;
            }
        }

        TimestampData.setMessageTimestamp(playerName, timestamp);
        return false;
    }
}

