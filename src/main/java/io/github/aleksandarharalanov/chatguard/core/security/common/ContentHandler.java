package io.github.aleksandarharalanov.chatguard.core.security.common;

import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ContentHandler {

    private ContentHandler() {}

    public static String sanitize(String content, List<String> termsWhitelist, List<String> regexWhitelist) {
        String sanitizedContent = content.toLowerCase();

        for (String term : termsWhitelist) {
            sanitizedContent = sanitizedContent.replaceAll("\\b" + Pattern.quote(term) + "\\b", "");
        }

        for (String regex : regexWhitelist) {
            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                sanitizedContent = matcher.replaceAll("");
            } catch (RuntimeException e) {
                LogUtil.logConsoleWarning(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }

        return sanitizedContent.trim();
    }

    public static String merge(String[] content) {
        return Stream.of(content)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}
