package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.security.common.ContentHandler;
import org.bukkit.entity.Player;

public final class FilterHandler {

    private FilterHandler() {}

    public static boolean isChatContentBlocked(Player player, String content) {
        return isBlocked(LogType.CHAT, player, content);
    }

    public static boolean isSignContentBlocked(Player player, String[] content) {
        return isBlocked(LogType.SIGN, player, ContentHandler.merge(content));
    }

    public static boolean isPlayerNameBlocked(Player player) {
        return isBlocked(LogType.NAME, player, player.getName());
    }

    private static boolean isBlocked(LogType logType, Player player, String content) {
        String sanitizedContent = ContentHandler.sanitize(content, FilterConfig.getTermsWhitelist(), FilterConfig.getRegexWhitelist());
        FilterResult result = FilterDetector.detect(sanitizedContent);

        if (result == null) {
            return false;
        }

        FilterFinalizer.finalizeActions(logType, player, content, result);
        return true;
    }
}
