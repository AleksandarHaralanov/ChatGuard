package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.LoginData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class NameEmbed extends DiscordEmbed {

    private final String trigger;

    public NameEmbed(JavaPlugin plugin, Player player, String content, String trigger) {
        super(plugin, player, content);
        this.trigger = trigger;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        String hex = ChatGuard.getDiscord().getString("customize.type.name.color");
        boolean censorData = ChatGuard.getDiscord().getBoolean("embed-log.optional.censor", true);

        embed.setTitle("Name Filter")
                .addField("Trigger:", String.format(censorData ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(hex));
    }

    @Override
    protected String getPlayerIP() {
        return LoginData.popPlayerIP(player.getName());
    }
}
