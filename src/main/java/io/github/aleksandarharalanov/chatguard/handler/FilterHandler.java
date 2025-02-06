package io.github.aleksandarharalanov.chatguard.handler;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.handler.LogHandler.performLogs;
import static io.github.aleksandarharalanov.chatguard.handler.PunishmentHandler.getMuteDuration;
import static io.github.aleksandarharalanov.chatguard.handler.PunishmentHandler.getStrike;
import static io.github.aleksandarharalanov.chatguard.handler.SoundHandler.playSoundCue;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static org.bukkit.Bukkit.getServer;

public class FilterHandler {

    private static String trigger;

    public static boolean checkPlayerMessage(Player player, String message) throws Exception {
        String sanitizedMessage = message.toLowerCase();
        Set<String> whitelistTerms = new HashSet<>(getConfig().getStringList("filter.rules.terms.whitelist", new ArrayList<>()));
        for (String term : whitelistTerms) sanitizedMessage = sanitizedMessage.replaceAll(term, "");
        if (containsBlacklistedTerms(sanitizedMessage) || matchesRegExPatterns(sanitizedMessage)) {
            cancelPlayerMessage(player, message);
            return true;
        }
        return false;
    }

    public static boolean shouldBlockName(String playerName) {
        String sanitizedMessage = playerName.toLowerCase();
        Set<String> whitelistTerms = new HashSet<>(getConfig().getStringList("filter.rules.terms.whitelist", new ArrayList<>()));
        for (String term : whitelistTerms) sanitizedMessage = sanitizedMessage.replaceAll(term, "");
        return containsBlacklistedTerms(sanitizedMessage) || matchesRegExPatterns(sanitizedMessage);
    }

    private static boolean containsBlacklistedTerms(String message) {
        Set<String> blacklistTerms = new HashSet<>(getConfig().getStringList("filter.rules.terms.blacklist", new ArrayList<>()));
        String[] messageTerms = message.split("\\s+");
        for (String term : messageTerms)
            if (blacklistTerms.contains(term)) {
                trigger = term;
                return true;
            }
        return false;
    }

    private static boolean matchesRegExPatterns(String message) {
        Set<String> regexList = new HashSet<>(getConfig().getStringList("filter.rules.regex", new ArrayList<>()));
        for (String regex : regexList) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                trigger = regex.replace("\\", "\\\\").replace("\"", "\\\"");
                return true;
            }
        }
        return false;
    }

    private static void cancelPlayerMessage(Player player, String message) throws Exception {
        boolean isWarnEnabled = getConfig().getBoolean("filter.warn-player", true);
        if (isWarnEnabled) player.sendMessage(translate("&cMessage cancelled for containing blocked words."));

        playSoundCue(player,false);
        performLogs("filter", player, message);
        issuePunishments(player);
    }

    private static void issuePunishments(Player player) throws Exception {
        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        boolean isMuteEnabled = getConfig().getBoolean("filter.mute.enabled", true);
        if (essentials != null && essentials.isEnabled() && isMuteEnabled) {
            User user = essentials.getUser(player.getName());
            user.setMuteTimeout(Util.parseDateDiff(getMuteDuration(player), true));
            user.setMuted(true);
            getServer().broadcastMessage(translate(String.format("&c[ChatGuard] %s muted for %s. by system; message contains blocked words.", player.getName(), getMuteDuration(player))));
        }

        if (getStrike(player) <= 4) {
            getStrikes().setProperty(player.getName(), getStrike(player) + 1);
            getStrikes().saveConfig();
        }
    }

    public static String getTrigger() {
        return trigger;
    }
}
