package io.github.aleksandarharalanov.chatguard.core.security.captcha;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.CaptchaData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public final class CaptchaDetector {

    private CaptchaDetector() {}

    public static boolean doesPlayerTriggerCaptcha(String playerName, String message) {
        String sanitizedMessage = message.toLowerCase();
        Set<String> whitelistTerms = new HashSet<>(ChatGuard.getConfig().getStringList("captcha.whitelist", new ArrayList<>()));

        for (String term : whitelistTerms) {
            sanitizedMessage = sanitizedMessage.replaceAll(term, "");
        }

        if (sanitizedMessage.isEmpty()) return false;

        LinkedList<String> messages = CaptchaData.getMessageHistory(playerName);
        messages.add(sanitizedMessage);

        if (messages.size() > CaptchaData.getThreshold()) {
            messages.removeFirst();
        }

        return messages.size() == CaptchaData.getThreshold() &&
                messages.stream().allMatch(msg -> msg.equals(messages.getFirst()));
    }
}
