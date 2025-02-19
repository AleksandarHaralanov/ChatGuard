package io.github.aleksandarharalanov.chatguard.core.log.logger;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.log.embed.*;
import io.github.aleksandarharalanov.chatguard.util.log.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

public final class DiscordLogger {

    private DiscordLogger() {}

    public static void log(LogType logType, Player player, String content, String trigger) {
        if (!isDiscordLogTypeEnabled(logType)) return;

        String webhookUrl = ChatGuard.getDiscord().getString("webhook-url");
        String webhookName = ChatGuard.getDiscord().getString(String.format(
                "customize.type.%s.webhook.name",
                logType.name().toLowerCase()
        ));
        String webhookIcon = ChatGuard.getDiscord().getString(String.format(
                "customize.type.%s.webhook.icon",
                logType.name().toLowerCase()
        ));

        DiscordUtil webhook = new DiscordUtil(webhookUrl);
        webhook.setUsername(webhookName);
        webhook.setAvatarUrl(webhookIcon);

        DiscordEmbed embed;
        switch (logType) {
            case CHAT:
                embed = new ChatEmbed(ChatGuard.getInstance(), player, content, trigger);
                break;
            case SIGN:
                embed = new SignEmbed(ChatGuard.getInstance(), player, content, trigger);
                break;
            case NAME:
                embed = new NameEmbed(ChatGuard.getInstance(), player, content, trigger);
                break;
            case CAPTCHA:
                embed = new CaptchaEmbed(ChatGuard.getInstance(), player, content);
                break;
            default:
                return;
        }

        webhook.addEmbed(embed.getEmbed());

        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(ChatGuard.getInstance(), () -> {
            try {
                webhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0L);
    }

    private static boolean isDiscordLogTypeEnabled(LogType logType) {
        switch (logType) {
            case CHAT:
                return ChatGuard.getDiscord().getBoolean("embed-log.type.chat", true);
            case SIGN:
                return ChatGuard.getDiscord().getBoolean("embed-log.type.sign", true);
            case NAME:
                return ChatGuard.getDiscord().getBoolean("embed-log.type.name", true);
            case CAPTCHA:
                return ChatGuard.getDiscord().getBoolean("embed-log.type.captcha", true);
            default:
                return false;
        }
    }
}
