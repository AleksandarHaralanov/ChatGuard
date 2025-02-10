package io.github.aleksandarharalanov.chatguard;

import io.github.aleksandarharalanov.chatguard.command.ChatGuardCommand;
import io.github.aleksandarharalanov.chatguard.listener.player.PlayerChatListener;
import io.github.aleksandarharalanov.chatguard.listener.player.PlayerCommandPreprocessListener;
import io.github.aleksandarharalanov.chatguard.listener.player.PlayerJoinListener;
import io.github.aleksandarharalanov.chatguard.listener.player.PlayerQuitListener;
import io.github.aleksandarharalanov.chatguard.util.ConfigUtil;
import io.github.aleksandarharalanov.chatguard.util.LoggerUtil;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;
import static io.github.aleksandarharalanov.chatguard.util.UpdateUtil.checkForUpdates;

public class ChatGuard extends JavaPlugin {

    private static ChatGuard plugin;
    private static ConfigUtil config;
    private static ConfigUtil strikes;

    @Override
    public void onEnable() {
        checkForUpdates(this, "https://api.github.com/repos/AleksandarHaralanov/ChatGuard/releases/latest");

        plugin = this;

        config = new ConfigUtil(this, "config.yml");
        config.load();

        strikes = new ConfigUtil(this, "strikes.yml");
        strikes.load();

        final LoggerUtil log = new LoggerUtil(this, "log.txt");
        log.initializeLog();

        PluginManager pM = getServer().getPluginManager();
        final PlayerCommandPreprocessListener pCPL = new PlayerCommandPreprocessListener();
        final PlayerChatListener pCL = new PlayerChatListener();
        final PlayerJoinListener pJL = new PlayerJoinListener();
        final PlayerQuitListener pQL = new PlayerQuitListener();
        pM.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pCPL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_PRELOGIN, pJL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_CHAT, pCL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_JOIN, pJL, Priority.Normal, this);
        pM.registerEvent(Type.PLAYER_JOIN, pJL, Priority.Normal, this);
        pM.registerEvent(Type.PLAYER_QUIT, pQL, Priority.Normal, this);

        final ChatGuardCommand command = new ChatGuardCommand(this);
        getCommand("chatguard").setExecutor(command);

        logInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        logInfo(String.format("[%s] v%s Disabled.", getDescription().getName(), getDescription().getVersion()));
    }

    public static ChatGuard getInstance() {
        return plugin;
    }

    public static ConfigUtil getConfig() {
        return config;
    }

    public static ConfigUtil getStrikes() {
        return strikes;
    }
}