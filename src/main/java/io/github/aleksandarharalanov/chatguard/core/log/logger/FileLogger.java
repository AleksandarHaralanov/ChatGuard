package io.github.aleksandarharalanov.chatguard.core.log.logger;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;
import org.bukkit.entity.Player;

public final class FileLogger {

    private FileLogger() {}

    public static void log(LogType logType, Player player, String content) {
        if (!shouldLocalFileLog(logType)) return;

        LogUtil.writeToLogFile(String.format(
                "[%s] [%s] <%s> %s",
                logType.name(),
                player.getAddress().getAddress().getHostAddress(),
                player.getName(),
                content
        ), true);
    }

    private static boolean shouldLocalFileLog(LogType logType) {
        boolean isLocalFileLogEnabled = ChatGuard.getConfig().getBoolean("filter.log.console", true);
        return isLocalFileLogEnabled && logType.hasAttribute(LogAttribute.FILTER) && logType != LogType.NAME;
    }
}
