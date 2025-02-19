package io.github.aleksandarharalanov.chatguard.core.data;

import java.util.HashMap;

public final class TimestampData {

    private static final HashMap<String, Long> playerCommandTimestamps = new HashMap<>();
    private static final HashMap<String, Long> playerMessageTimestamps = new HashMap<>();

    private TimestampData() {}

    public static long getCommandTimestamp(String playerName) {
        return playerCommandTimestamps.getOrDefault(playerName, 0L);
    }

    public static void setCommandTimestamp(String playerName, long timestamp) {
        playerCommandTimestamps.put(playerName, timestamp);
    }

    public static long getMessageTimestamp(String playerName) {
        return playerMessageTimestamps.getOrDefault(playerName, 0L);
    }

    public static void setMessageTimestamp(String playerName, long timestamp) {
        playerMessageTimestamps.put(playerName, timestamp);
    }
}
