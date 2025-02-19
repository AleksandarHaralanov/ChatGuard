package io.github.aleksandarharalanov.chatguard.core.data;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;

public final class CaptchaData {

    private static final HashMap<String, LinkedList<String>> playerMessages = new HashMap<>();

    private CaptchaData() {}

    public static HashMap<String, LinkedList<String>> getPlayerMessages() {
        return playerMessages;
    }

    public static LinkedList<String> getMessageHistory(String playerName) {
        return playerMessages.computeIfAbsent(playerName, k -> new LinkedList<>());
    }

    public static void removePlayerCaptcha(String playerName) {
        ChatGuard.getCaptchas().removeProperty(playerName);
        ChatGuard.getCaptchas().save();
    }

    public static void setPlayerCaptcha(String playerName, String captchaCode) {
        ChatGuard.getCaptchas().setProperty(playerName, captchaCode);
        ChatGuard.getCaptchas().save();
    }

    public static String getPlayerCaptcha(String playerName) {
        return ChatGuard.getCaptchas().getString(playerName);
    }

    public static int getThreshold() {
        return ChatGuard.getConfig().getInt("captcha.threshold", 5);
    }

    public static int getCodeLength() {
        return ChatGuard.getConfig().getInt("captcha.code.length", 5);
    }

    public static String getCodeCharacters() {
        return ChatGuard.getConfig().getString("captcha.code.characters", "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789");
    }
}
