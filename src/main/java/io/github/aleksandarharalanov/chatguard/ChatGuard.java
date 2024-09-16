package io.github.aleksandarharalanov.chatguard;

import io.github.aleksandarharalanov.chatguard.command.ChatGuardCommand;
import io.github.aleksandarharalanov.chatguard.listener.PlayerChatListener;
import io.github.aleksandarharalanov.chatguard.util.ConfigUtil;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;
import static io.github.aleksandarharalanov.chatguard.util.UpdateUtil.checkForUpdates;

public class ChatGuard extends JavaPlugin {

    private static ChatGuard plugin;
    private static ConfigUtil config;
    private static PluginDescriptionFile pdf;

    @Override
    public void onEnable() {
        pdf = getDescription();

        checkForUpdates(pdf.getName(), pdf.getVersion(),
                "https://api.github.com/repos/AleksandarHaralanov/ChatGuard/releases/latest"
        );

        plugin = this;

        config = new ConfigUtil(this, "config.yml");
        config.loadConfig();

        getCommand("chatguard").setExecutor(new ChatGuardCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        final PlayerChatListener playerChatListener = new PlayerChatListener();
        pluginManager.registerEvent(Type.PLAYER_CHAT, playerChatListener, Priority.Normal, this);

        logInfo(String.format("[%s] v%s Enabled.", pdf.getName(), pdf.getVersion()));
    }

    @Override
    public void onDisable() {
        config.saveConfig();

        logInfo(String.format("[%s] v%s Disabled.", pdf.getName(), pdf.getVersion()));
    }

    public static ChatGuard getInstance() {
        return plugin;
    }

    public static ConfigUtil getConfig() {
        return config;
    }
}