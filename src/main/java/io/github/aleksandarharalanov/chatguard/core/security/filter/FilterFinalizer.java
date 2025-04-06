package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.log.logger.ConsoleLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.DiscordLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.FileLogger;
import io.github.aleksandarharalanov.chatguard.core.misc.AudioCuePlayer;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class FilterFinalizer {

    private FilterFinalizer() {}

    public static void finalizeActions(LogType logType, Player player, String content, FilterResult result) {
        if (shouldWarnPlayer(logType)) {
            player.sendMessage(ColorUtil.translateColorCodes(getWarningMessage(logType)));
        }

        AudioCuePlayer.play(logType, player, false);
        ConsoleLogger.log(logType, player, content);
        FileLogger.log(logType, player, content);

        // Underline the match for easier troubleshooting
        content = content.replaceFirst(result.getMatch(), String.format("__%s__", result.getMatch()));
        DiscordLogger.log(logType, player, content, result.getTrigger());

        PenaltyEnforcer.processMute(logType, player);
        PenaltyEnforcer.incrementStrikeTier(logType, player);
    }

    private static boolean shouldWarnPlayer(LogType logType) {
        return logType.hasAttribute(LogAttribute.WARN) && FilterConfig.getWarnPlayerEnabled();
    }

    private static String getWarningMessage(LogType logType) {
        if (logType == LogType.SIGN) {
            return "&cSign censored due to bad words.";
        } else {
            return "&cMessage cancelled due to bad words.";
        }
    }
}
