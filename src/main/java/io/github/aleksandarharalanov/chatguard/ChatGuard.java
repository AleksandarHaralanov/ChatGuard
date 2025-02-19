package io.github.aleksandarharalanov.chatguard;

import io.github.aleksandarharalanov.chatguard.command.ChatGuardCommand;
import io.github.aleksandarharalanov.chatguard.listener.block.SignChangeListener;
import io.github.aleksandarharalanov.chatguard.listener.player.*;
import io.github.aleksandarharalanov.chatguard.util.config.ConfigUtil;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;
import io.github.aleksandarharalanov.chatguard.util.log.UpdateUtil;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatGuard extends JavaPlugin {

    private static ChatGuard plugin;
    private static ConfigUtil config;
    private static ConfigUtil discord;
    private static ConfigUtil strikes;
    private static ConfigUtil captchas;

    @Override
    public void onEnable() {
        UpdateUtil.checkAvailablePluginUpdates(this, "https://api.github.com/repos/AleksandarHaralanov/ChatGuard/releases/latest");

        plugin = this;

        // Configurations
        config = new ConfigUtil(this, "config/config.yml");
        config.load();
        discord = new ConfigUtil(this, "config/discord.yml");
        discord.load();

        // Data
        strikes = new ConfigUtil(this, "data/strikes.yml");
        strikes.load();
        captchas = new ConfigUtil(this, "data/captchas.yml");
        captchas.load();

        // Local File Log
        final LogUtil log = new LogUtil(this, "log.txt");
        log.initializeLogFile();

        PluginManager pM = getServer().getPluginManager();

        // Player Listeners
        final PlayerCommandPreprocessListener pCPL = new PlayerCommandPreprocessListener();
        final PlayerChatListener pCL = new PlayerChatListener();
        final PlayerJoinListener pJL = new PlayerJoinListener();
        final PlayerLoginListener pLL = new PlayerLoginListener();
        final PlayerQuitListener pQL = new PlayerQuitListener();
        pM.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pCPL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_LOGIN, pLL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_CHAT, pCL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_JOIN, pJL, Priority.Lowest, this);
        pM.registerEvent(Type.PLAYER_QUIT, pQL, Priority.Lowest, this);

        // Block Listeners
        final SignChangeListener sCL = new SignChangeListener();
        pM.registerEvent(Type.SIGN_CHANGE, sCL, Priority.Lowest, this);

        // Main Command
        final ChatGuardCommand command = new ChatGuardCommand(this);
        getCommand("chatguard").setExecutor(command);

        LogUtil.logConsoleInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        LogUtil.logConsoleInfo(String.format("[%s] v%s Disabled.", getDescription().getName(), getDescription().getVersion()));
    }

    public static ChatGuard getInstance() {
        return plugin;
    }

    public static ConfigUtil getConfig() {
        return config;
    }

    public static ConfigUtil getDiscord() {
        return discord;
    }

    public static ConfigUtil getStrikes() {
        return strikes;
    }

    public static ConfigUtil getCaptchas() {
        return captchas;
    }
}