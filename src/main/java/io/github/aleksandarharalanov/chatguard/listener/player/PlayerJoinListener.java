package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.security.filter.ContentFilter;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Fallback if PlayerLoginEvent fails
        boolean isNameFilterEnabled = ChatGuard.getConfig().getBoolean("filter.enabled.name", true);
        if (isNameFilterEnabled && ContentFilter.isPlayerNameBlocked(player)) {
            player.kickPlayer(ColorUtil.translateColorCodes("&cName contains bad words."));
            return;
        }

        PenaltyData.setDefaultStrikeTier(player);
    }
}
