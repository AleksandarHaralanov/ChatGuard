package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.getPlayerMessages;

public class PlayerQuitListener extends PlayerListener {

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        getPlayerMessages().remove(playerName);
    }
}
