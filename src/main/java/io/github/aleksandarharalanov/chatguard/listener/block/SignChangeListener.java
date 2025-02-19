package io.github.aleksandarharalanov.chatguard.listener.block;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.security.filter.ContentFilter;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener extends BlockListener {

    @Override
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (hasBypassPermission(player)) return;
        if (handleSignFiltering(player, event)) return;
    }

    private static boolean hasBypassPermission(Player player) {
        return AccessUtil.senderHasPermission(player, "chatguard.bypass");
    }

    private static boolean handleSignFiltering(Player player, SignChangeEvent event) {
        boolean isSignFilterEnabled = ChatGuard.getConfig().getBoolean("filter.enabled.sign", true);
        if (isSignFilterEnabled && ContentFilter.isSignContentBlocked(player, event.getLines())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
