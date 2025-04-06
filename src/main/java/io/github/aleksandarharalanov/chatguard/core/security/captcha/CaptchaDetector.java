package io.github.aleksandarharalanov.chatguard.core.security.captcha;

import io.github.aleksandarharalanov.chatguard.core.config.CaptchaConfig;
import io.github.aleksandarharalanov.chatguard.core.data.MessageData;
import io.github.aleksandarharalanov.chatguard.core.security.common.ContentHandler;

import java.util.LinkedList;

public final class CaptchaDetector {

    private CaptchaDetector() {}

    public static boolean doesPlayerTriggerCaptcha(String playerName, String content) {
        String sanitizedContent = ContentHandler.sanitize(content, CaptchaConfig.getTermsWhitelist(), CaptchaConfig.getRegexWhitelist());

        if (sanitizedContent.isEmpty()) {
            return false;
        }

        LinkedList<String> messages = MessageData.getMessageHistory(playerName);
        messages.add(sanitizedContent);

        if (messages.size() > CaptchaConfig.getThreshold()) {
            messages.removeFirst();
        }

        return messages.size() == CaptchaConfig.getThreshold() &&
                messages.stream().allMatch(msg -> msg.equals(messages.getFirst()));
    }
}