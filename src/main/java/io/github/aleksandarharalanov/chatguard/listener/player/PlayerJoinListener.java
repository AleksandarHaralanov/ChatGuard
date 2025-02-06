package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.shouldBlockName;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        if (getStrikes().getInt(playerName, -1) == -1) {
            getStrikes().setProperty(playerName, 0);
            getStrikes().saveConfig();
        }

        // this may be slightly redundant, but i will add it here just in case the pre-login event somehow fails
        if (shouldBlockName(playerName)) {
            event.getPlayer().kickPlayer(ChatColor.RED + "Username contains blacklisted terms.");
        }
    }

    @Override
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        String playerName = event.getName();
        if (shouldBlockName(playerName)) {
            event.disallow(PlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Username contains blacklisted terms.");
        }
    }
}
