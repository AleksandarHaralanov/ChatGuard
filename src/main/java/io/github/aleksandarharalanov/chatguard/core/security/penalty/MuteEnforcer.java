package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MuteEnforcer {

    private MuteEnforcer() {}

    public static void processEssentialsMute(LogType logType, Player player) {
        boolean isEssentialsMuteEnabled = ChatGuard.getConfig().getBoolean("filter.essentials-mute.enabled", true);
        if (!isEssentialsMuteEnabled) return;

        if (!logType.hasAttribute(LogAttribute.MUTE) || logType == LogType.NAME) return;

        Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null || !essentials.isEnabled()) return;

        User user = essentials.getUser(player.getName());
        try {
            user.setMuteTimeout(Util.parseDateDiff(PenaltyData.getMuteDuration(player), true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setMuted(true);

        Bukkit.getServer().broadcastMessage(ColorUtil.translateColorCodes(String.format(
                "&c[ChatGuard] %s muted for %s. by system; content has bad words.",
                player.getName(), PenaltyData.getMuteDuration(player)
        )));
    }
}
