package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.log.logger.ConsoleLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.DiscordLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.FileLogger;
import io.github.aleksandarharalanov.chatguard.core.misc.AudioCuePlayer;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.MuteEnforcer;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.StrikeEnforcer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class ContentHandler {

    private ContentHandler() {}

    public static void handleBlockedContent(LogType logType, Player player, String content, String trigger) {
        if (shouldWarnPlayer(logType)) {
            player.sendMessage(ColorUtil.translateColorCodes(getWarningMessage(logType)));
        }

        AudioCuePlayer.play(logType, player, false);
        ConsoleLogger.log(logType, player, content);
        FileLogger.log(logType, player, content);
        DiscordLogger.log(logType, player, content, trigger);
        MuteEnforcer.processEssentialsMute(logType, player);
        StrikeEnforcer.incrementStrikeTier(logType, player);
    }

    private static boolean shouldWarnPlayer(LogType logType) {
        return logType != LogType.NAME && ChatGuard.getConfig().getBoolean("filter.warn-player", true);
    }

    private static String getWarningMessage(LogType logType) {
        switch (logType) {
            case SIGN:
                return "&cSign censored due to bad words.";
            default:
                return "&cMessage cancelled due to bad words.";
        }
    }
}
