package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class ChatEmbed extends DiscordEmbed {

    private final String trigger;

    public ChatEmbed(JavaPlugin plugin, Player player, String content, String trigger) {
        super(plugin, player, content);
        this.trigger = trigger;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        String hex = ChatGuard.getDiscord().getString("customize.type.chat.color");
        boolean censorData = ChatGuard.getDiscord().getBoolean("embed-log.optional.censor", true);

        if (!PenaltyData.isPlayerOnFinalStrike(player)) {
            embed.setDescription(String.format(
                    "S%d ► S%d ・ Mute Duration: %s",
                    PenaltyData.getStrike(player), PenaltyData.getStrike(player) + 1, PenaltyData.getMuteDuration(player)
            ));
        } else {
            embed.setDescription(String.format(
                    "S%d (Max) ・ Mute Duration: %s",
                    PenaltyData.getStrike(player), PenaltyData.getMuteDuration(player)
            ));
        }

        embed.setTitle("Chat Filter")
                .addField("Content:", content, false)
                .addField("Trigger:", String.format(censorData ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(hex));
    }
}
