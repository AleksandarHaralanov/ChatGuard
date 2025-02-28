package io.github.aleksandarharalanov.chatguard.core.data;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import org.bukkit.entity.Player;

public final class PenaltyData {

    private PenaltyData() {}

    public static int getStrike(String playerName) {
        return ChatGuard.getStrikes().getInt(playerName, 0);
    }

    public static int getStrike(Player player) {
        return ChatGuard.getStrikes().getInt(player.getName(), 0);
    }

    public static String getMuteDuration(String playerName) {
        return ChatGuard.getConfig().getString(String.format(
                "filter.auto-mute.duration.s%d",
                ChatGuard.getStrikes().getInt(playerName, 0)
        ));
    }

    public static String getMuteDuration(Player player) {
        return ChatGuard.getConfig().getString(String.format(
                "filter.auto-mute.duration.s%d",
                ChatGuard.getStrikes().getInt(player.getName(), 0)
        ));
    }

    public static boolean isPlayerOnFinalStrike(String playerName) {
        return ChatGuard.getStrikes().getInt(playerName, 0) == 5;
    }

    public static boolean isPlayerOnFinalStrike(Player player) {
        return ChatGuard.getStrikes().getInt(player.getName(), 0) == 5;
    }

    public static void setDefaultStrikeTier(Player player) {
        if (ChatGuard.getStrikes().getInt(player.getName(), -1) == -1) {
            ChatGuard.getStrikes().setProperty(player.getName(), 0);
            ChatGuard.getStrikes().save();
        }
    }
}
