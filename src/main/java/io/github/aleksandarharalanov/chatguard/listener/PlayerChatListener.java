package io.github.aleksandarharalanov.chatguard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.getFilter;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        boolean toggle = getConfig().getBoolean("chatguard.toggle", true);
        if (!toggle) {
            return;
        }

        Player player = event.getPlayer();
        if (hasPermission(player, "chatguard.bypass")) {
            return;
        }

        String message = event.getMessage().toLowerCase();
        for (String badWord : getFilter()) {
            if (message.contains(badWord)) {
                event.setCancelled(true);
                player.sendMessage(translate("&cYour message has been filtered from bad words."));
                player.sendMessage(translate("&cThis has been logged to operators."));
                logWarning(String.format("[ChatGuard] %s: %s", player.getName(), badWord));
                return;
            }
        }
    }
}
