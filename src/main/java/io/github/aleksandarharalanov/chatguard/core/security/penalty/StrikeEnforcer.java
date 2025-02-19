package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;

public final class StrikeEnforcer {

    public static void incrementStrikeTier(LogType logType, Player player) {
        if (!logType.hasAttribute(LogAttribute.STRIKE) || logType == LogType.NAME) return;

        if (PenaltyData.getStrike(player) <= 4) {
            ChatGuard.getStrikes().setProperty(player.getName(), PenaltyData.getStrike(player) + 1);
            ChatGuard.getStrikes().save();
        }
    }
}
