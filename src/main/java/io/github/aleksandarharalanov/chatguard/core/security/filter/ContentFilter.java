package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ContentFilter {

    private ContentFilter() {}

    public static boolean isChatContentBlocked(Player player, String content) {
        return isBlocked(LogType.CHAT, player, content);
    }

    public static boolean isSignContentBlocked(Player player, String[] content) {
        return isBlocked(LogType.SIGN, player, mergeSignContent(content));
    }

    public static boolean isPlayerNameBlocked(Player player) {
        return isBlocked(LogType.NAME, player, player.getName());
    }

    private static boolean isBlocked(LogType logType, Player player, String content) {
        String sanitizedContent = sanitizeContent(content);
        String trigger = TriggerDetector.getTrigger(sanitizedContent);

        if (trigger == null) return false;

        ContentHandler.handleBlockedContent(logType, player, content, trigger);
        return true;
    }

    private static String sanitizeContent(String content) {
        String sanitizedContent = content.toLowerCase();
        Set<String> whitelistTerms = new HashSet<>(ChatGuard.getConfig().getStringList("filter.rules.terms.whitelist", new ArrayList<>()));

        for (String term : whitelistTerms) {
            sanitizedContent = sanitizedContent.replaceAll(term, "");
        }
        return sanitizedContent;
    }

    private static String mergeSignContent(String[] content) {
        return Stream.of(content)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}
