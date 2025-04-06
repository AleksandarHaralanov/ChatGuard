package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterDetector {

    private FilterDetector() {}

    public static FilterResult detect(String sanitizedContent) {
        FilterResult result = checkBlacklistedTerms(sanitizedContent);
        return (result != null) ? result : checkRegexPatterns(sanitizedContent);
    }

    private static FilterResult checkBlacklistedTerms(String sanitizedContent) {
        String[] contentTerms = sanitizedContent.split("\\s+");
        for (String term : contentTerms) {
            if (FilterConfig.getTermsBlacklist().contains(term)) {
                return new FilterResult(term, term);
            }
        }
        return null;
    }

    private static FilterResult checkRegexPatterns(String sanitizedContent) {
        for (String regex : FilterConfig.getRegexBlacklist()) {
            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                if (matcher.find()) {
                    String matchedText = matcher.group(0);
                    String triggerValue = regex.replace("\\", "\\\\").replace("\"", "\\\"");
                    return new FilterResult(triggerValue, matchedText);
                }
            } catch (RuntimeException e) {
                LogUtil.logConsoleWarning(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }
        return null;
    }
}