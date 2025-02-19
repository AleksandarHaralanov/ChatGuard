package io.github.aleksandarharalanov.chatguard.core.security.spam;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.TimestampData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class CommandRateLimiter {

    private CommandRateLimiter() {}

    public static boolean isPlayerCommandSpamming(Player player) {
        long timestamp = System.currentTimeMillis();
        String playerName = player.getName();

        long lastTimestamp = TimestampData.getCommandTimestamp(playerName);

        if (lastTimestamp != 0) {
            long elapsed = timestamp - lastTimestamp;

            int strikeTier = ChatGuard.getStrikes().getInt(playerName, 0);
            int cooldown = ChatGuard.getConfig().getInt(String.format("spam-prevention.cooldown-ms.command.s%d", strikeTier), 0);

            if (elapsed <= cooldown) {
                boolean isWarnEnabled = ChatGuard.getConfig().getBoolean("spam-prevention.warn-player", true);
                if (isWarnEnabled) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&cPlease wait %.2f sec. before running another command.", remainingTime
                    )));
                }
                return true;
            }
        }

        TimestampData.setCommandTimestamp(playerName, timestamp);
        return false;
    }
}
