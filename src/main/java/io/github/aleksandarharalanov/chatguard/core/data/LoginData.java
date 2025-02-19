package io.github.aleksandarharalanov.chatguard.core.data;

import java.util.HashMap;
import java.util.Map;

public final class LoginData {

    private static final Map<String, String> playerPreLoginIPs = new HashMap<>();

    private LoginData() {}

    public static void storePlayerIP(String playerName, String playerIp) {
        playerPreLoginIPs.put(playerName.toLowerCase(), playerIp);
    }

    public static String popPlayerIP(String playerName) {
        return playerPreLoginIPs.remove(playerName.toLowerCase());
    }
}