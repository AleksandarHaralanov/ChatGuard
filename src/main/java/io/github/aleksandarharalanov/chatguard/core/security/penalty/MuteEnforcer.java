package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import com.earth2me.essentials.Essentials;
import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.misc.TimeFormatter;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public final class MuteEnforcer {

    public static final MuteHandler muteHandler;

    static {
        PluginManager pM = Bukkit.getServer().getPluginManager();
        if (pM.getPlugin("Essentials") != null) {
            muteHandler = new EssentialsMuteHandler((Essentials) pM.getPlugin("Essentials"));
        } else if (pM.getPlugin("ZCore") != null) {
            muteHandler = new ZCoreMuteHandler();
        } else {
            muteHandler = null;
        }
    }

    private MuteEnforcer() {}

    public static void processMute(LogType logType, Player player) {
        boolean isAutomaticMuteEnabled = ChatGuard.getConfig().getBoolean("filter.auto-mute.enabled", true);
        if (!isAutomaticMuteEnabled) return;

        if (!logType.hasAttribute(LogAttribute.MUTE) || logType == LogType.NAME) return;

        if (muteHandler == null) return;

        try {
            muteHandler.setUserMuteTimeout(
                    player.getName(),
                    TimeFormatter.parseDateDiff(PenaltyData.getMuteDuration(player), true));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Bukkit.getServer().broadcastMessage(ColorUtil.translateColorCodes(String.format(
                "&c[ChatGuard] %s muted for %s. by system; content has bad words.",
                player.getName(), PenaltyData.getMuteDuration(player)
        )));
    }
}
