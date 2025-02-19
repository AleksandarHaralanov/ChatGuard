package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.security.filter.ContentFilter;
import io.github.aleksandarharalanov.chatguard.core.data.LoginData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener extends PlayerListener {

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            LoginData.storePlayerIP(player.getName(), event.getKickMessage());
        }

        if (ContentFilter.isPlayerNameBlocked(player)) {
            event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    ColorUtil.translateColorCodes("&cName contains bad words.")
            );
        }
    }
}
