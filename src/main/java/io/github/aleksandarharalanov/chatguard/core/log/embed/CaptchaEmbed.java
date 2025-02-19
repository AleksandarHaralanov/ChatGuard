package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.CaptchaData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class CaptchaEmbed extends DiscordEmbed {

    public CaptchaEmbed(JavaPlugin plugin, Player player, String content) {
        super(plugin, player, content);
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        String hex = ChatGuard.getDiscord().getString("customize.type.captcha.color");
        embed.setTitle("Captcha Trigger")
                .setDescription(String.format("Repeated Content ・ %dx", CaptchaData.getThreshold()))
                .addField("Content:", content, false)
                .setColor(Color.decode(hex));
    }
}
