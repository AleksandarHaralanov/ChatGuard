package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.ChatGuard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TriggerDetector {

    private TriggerDetector() {}

    public static String getTrigger(String sanitizedContent) {
        String trigger = checkBlacklistedTerms(sanitizedContent);
        return (trigger != null) ? trigger : checkRegExPatterns(sanitizedContent);
    }

    private static String checkBlacklistedTerms(String sanitizedContent) {
        Set<String> blacklistTerms = new HashSet<>(ChatGuard.getConfig().getStringList("filter.rules.terms.blacklist", new ArrayList<>()));
        String[] contentTerms = sanitizedContent.split("\\s+");

        for (String term : contentTerms) {
            if (blacklistTerms.contains(term)) {
                return term;
            }
        }
        return null;
    }

    private static String checkRegExPatterns(String sanitizedContent) {
        Set<String> regexList = new HashSet<>(ChatGuard.getConfig().getStringList("filter.rules.regex", new ArrayList<>()));

        for (String regex : regexList) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sanitizedContent);
            if (matcher.find()) {
                return regex.replace("\\", "\\\\").replace("\"", "\\\"");
            }
        }
        return null;
    }
}