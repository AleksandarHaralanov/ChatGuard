package io.github.aleksandarharalanov.chatguard.handler;

import org.bukkit.entity.Player;

import java.util.*;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.handler.LogHandler.performLogs;
import static io.github.aleksandarharalanov.chatguard.handler.SoundHandler.playSoundCue;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static org.bukkit.Bukkit.getServer;

public class CaptchaHandler {

    private static final HashMap<String, LinkedList<String>> playerMessages = new HashMap<>();
    private static final HashMap<String, String> playerCaptcha = new HashMap<>();

    public static boolean isPlayerCaptchaActive(Player player) {
        String captchaCode = playerCaptcha.get(player.getName());
        if (captchaCode != null) {
            player.sendMessage(translate(String.format("&c[ChatGuard] Bot-like behavior detected. Code &b%s&c.", captchaCode)));
            player.sendMessage(translate("&cUse &e/cg captcha <code> &cto verify. Case-sensitive!"));
            playSoundCue(player, false);
            return true;
        }
        return false;
    }

    public static boolean checkPlayerCaptcha(Player player, String message) {
        String sanitizedMessage = message.toLowerCase();

        Set<String> whitelistTerms = new HashSet<>(getConfig().getStringList("captcha.whitelist", new ArrayList<>()));
        for (String term : whitelistTerms) sanitizedMessage = sanitizedMessage.replaceAll(term, "");

        if (sanitizedMessage.equalsIgnoreCase("")) return false;

        LinkedList<String> messages = playerMessages.computeIfAbsent(player.getName(), k -> new LinkedList<>());
        messages.add(sanitizedMessage);

        int threshold = getConfig().getInt("captcha.threshold", 4);
        if (messages.size() > threshold) messages.removeFirst();

        if (messages.size() == threshold && messages.stream().allMatch(msg -> msg.equals(messages.getFirst()))) {
            String captchaCode = generateCaptchaCode();
            playerCaptcha.put(player.getName(), captchaCode);

            player.sendMessage(translate(String.format("&c[ChatGuard] Bot-like behavior detected. Code &b%s&c.", captchaCode)));
            player.sendMessage(translate("&cUse &e/cg captcha <code> &cto verify. Case-sensitive!"));

            playSoundCue(player,false);
            performLogs("captcha", player, message);

            for (Player pl : getServer().getOnlinePlayers())
                if (hasPermission(pl, "chatguard.captcha"))
                    pl.sendMessage(translate(String.format("&c[ChatGuard] &e%s&c prompted captcha for bot-like behavior.", player.getName())));

            return true;
        }

        return false;
    }

    private static String generateCaptchaCode() {
        String chars = getConfig().getString("captcha.code.characters");
        int length = getConfig().getInt("captcha.code.length", 5);
        StringBuilder captcha = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) captcha.append(chars.charAt(random.nextInt(chars.length())));
        return captcha.toString();
    }

    public static HashMap<String, String> getPlayerCaptcha() {
        return playerCaptcha;
    }

    public static HashMap<String, LinkedList<String>> getPlayerMessages() {
        return playerMessages;
    }
}
