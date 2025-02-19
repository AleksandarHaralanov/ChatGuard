package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.util.log.DiscordUtil;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DiscordEmbed {

    protected final String pluginVersion;
    protected final Player player;
    protected final String content;
    protected final long timestamp;
    protected final DiscordUtil.EmbedObject embed;

    public DiscordEmbed(JavaPlugin plugin, Player player, String content) {
        this.pluginVersion = plugin.getDescription().getVersion();
        this.player = player;
        this.content = content;
        this.timestamp = System.currentTimeMillis() / 1000;
        this.embed = new DiscordUtil.EmbedObject();
    }

    protected void setupBaseEmbed() {
        String avatar = ChatGuard.getDiscord()
                .getString("customize.player-avatar")
                .replace("%player%", player.getName());
        boolean censorData = ChatGuard.getDiscord().getBoolean("embed-log.optional.censor", true);
        boolean logIPAddress = ChatGuard.getDiscord().getBoolean("embed-log.optional.data.ip-address", true);
        boolean logTimestamp = ChatGuard.getDiscord().getBoolean("embed-log.optional.data.timestamp", true);

        embed.setAuthor(player.getName(), null, avatar);

        setupEmbedDetails();

        if (logIPAddress) {
            embed.addField("IP:", String.format(censorData ? "||%s||" : "%s", getPlayerIP()), true);
        }

        if (logTimestamp) {
            embed.addField("Timestamp:", String.format("<t:%d:f>", timestamp), true);
        }

        embed.setFooter(
                String.format("ChatGuard v%s ・ Logger", pluginVersion),
                "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
        );
    }

    protected String getPlayerIP() {
        if (((CraftPlayer) player).getHandle().netServerHandler == null) {
            return "N/A";
        }
        return player.getAddress().getAddress().getHostAddress();
    }

    protected abstract void setupEmbedDetails();

    public DiscordUtil.EmbedObject getEmbed() {
        return embed;
    }
}
