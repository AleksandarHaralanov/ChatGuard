package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.data.PenaltyData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class SignEmbed extends DiscordEmbed {

    private final String trigger;

    public SignEmbed(JavaPlugin plugin, Player player, String content, String trigger) {
        super(plugin, player, content);
        this.trigger = trigger;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        String hex = ChatGuard.getDiscord().getString("customize.type.sign.color");
        boolean censorData = ChatGuard.getDiscord().getBoolean("embed-log.optional.censor", true);

        if (!PenaltyData.isPlayerOnFinalStrike(player)) {
            embed.setDescription(String.format(
                    "S%d ► S%d", PenaltyData.getStrike(player), PenaltyData.getStrike(player) + 1
            ));
        } else {
            embed.setDescription(String.format(
                    "S%d (Max)", PenaltyData.getStrike(player)
            ));
        }

        embed.setTitle("Sign Filter")
                .addField("Content:", content, false)
                .addField("Trigger:", String.format(censorData ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(hex));
    }
}
